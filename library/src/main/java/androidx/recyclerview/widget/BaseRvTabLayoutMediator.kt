package androidx.recyclerview.widget

import android.util.Log
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by Virogu
 * Date 2023/06/30 上午 10:14:28
 *
 * RecyclerView实现的Tab选项卡和ViewPager联动的工具
 *
 * ```
 * //ViewPager
 * viewPager2.adapter = object : FragmentStateAdapter{
 * ...
 * }
 * recyclerViewTab.layoutManager = LinearLayoutManager(requireContext())
 * val tabLayoutMediator = object: BaseRvTabLayoutMediator<XXTabItemBinding>(recyclerView, viewPager) {
 *  ...
 * }
 * tabLayoutMediator.attach()
 *
 * ...
 * //detach when destroyed
 * tabLayoutMediator.detach()
 * ```
 */

interface RvTabLayoutMediator<VB : ViewBinding> {

    /**
     * ViewPager和RecyclerView关联起来
     */
    fun attach()

    /**
     * ViewPager和RecyclerView解除关联
     */
    fun detach()

    /**
     * 选中指定Tab
     * @param position 要选中的Tab的index
     */
    fun selectTab(position: Int)

    /**
     * 对指定index的Tab进行更新操作
     * @param position 要选中的Tab的index
     * @param doUpdate 找到指定index的Tab后需要执行的操作
     */
    fun updateViewAt(position: Int, doUpdate: VB.(Boolean) -> Unit)

    /**
     * 更新指定index,
     * 执行后会通过 [BaseRvTabLayoutMediator.onBindView] 刷新指定位置的Tab界面
     * @param position 要刷新的Tab的index
     * @param payload 可选的部分更新参数，为空时全量更新
     */
    fun notifyItemChanged(position: Int, payload: Any? = null)
}

/**
 *
 * @param VB ViewBinding 自定义的Tab布局
 *
 * @param recyclerView [RecyclerView] 要展示Tab选项卡的RecyclerView
 * @param viewPager [ViewPager2] 要关联的ViewPager
 * @param listenItemFullVisible 是否需要监听首尾的Item有没有完全显示出来. default: false
 *
 **/
abstract class BaseRvTabLayoutMediator<VB : ViewBinding> constructor(
    private val recyclerView: RecyclerView,
    private val viewPager: ViewPager2,
    private val listenItemFullVisible: Boolean = false
) : RvTabLayoutMediator<VB> {

    companion object {
        private const val TAG = "RvTabLayoutMediator"
    }

    /**
     * 创建Tab的Item布局的操作
     * @return ViewBinding
     *
     * [RecyclerView.Adapter.onCreateViewHolder]
     *
     * XXXViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
     */
    abstract fun onCreateView(parent: ViewGroup): VB

    /**
     * Tab的Item界面数据绑定操作
     * @param position item索引
     * @param isSelected item是否是选中状态
     * @param payloads 局部更新时的payload，为空时需要完全更新
     *
     * [RecyclerView.Adapter.onBindViewHolder]
     */
    abstract fun VB.onBindView(position: Int, isSelected: Boolean, payloads: Set<Any>)

    /**
     * 选择某个Tab时是否进行拦截
     * @param mediator [RvTabLayoutMediator]
     * @param position 当前准备选中的item索引
     * @param lastPosition 上次选中的item索引
     * lastPosition 和 position 相同的话表示此次是ViewPager数据变化导致的Tab重新选中
     * @return Boolean true 表示拦截此次选中，即禁止选中此Item；false 表示不拦截此次选中操作
     */
    open fun interceptTabSelected(
        mediator: RvTabLayoutMediator<VB>,
        position: Int,
        lastPosition: Int
    ): Boolean {
        return false
    }

    /**
     * Tab的首部Item和尾部Item是否完全可见
     * 需要 [listenItemFullVisible] 参数值为 true 时才会添加监听
     *
     * @param firstFullVisible 首部Item是否完全可见
     * @param lastFullVisible 尾部Item是否完全可见
     */
    open fun onItemFullVisible(firstFullVisible: Boolean, lastFullVisible: Boolean) {

    }

    private var isAttached = false
    private val vpAdapter get() = viewPager.adapter
    private val rvAdapter = RvTabLayoutAdapter()

    private val dataSetChangeObserver = DataSetChangeObserver {
        refreshRvAdapter()
    }

    private val vpOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            logD("onPageSelected: $position")
            rvAdapter.updateSelect(position)
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //logD("onScrollStateChanged: $newState")
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                notifyVisibility()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            //logD("onScrolled: dx: $dx, dy: $dy")
            //列表刷新bindView结束后会触发 onScrolled(recyclerView, 0, 0), 获取一下可见性
            if (dx == 0 && dy == 0) {
                notifyVisibility()
            }
        }
    }

    override fun attach() {
        if (isAttached) {
            return
        }
        isAttached = true
        logD("attached")
        vpAdapter?.registerAdapterDataObserver(dataSetChangeObserver)
        viewPager.registerOnPageChangeCallback(vpOnPageChangeCallback)
        recyclerView.adapter = rvAdapter
        recyclerView.addScrollListener()
        refreshRvAdapter()
    }

    override fun detach() {
        if (!isAttached) {
            return
        }
        isAttached = false
        recyclerView.removeScrollListener()
        try {
            vpAdapter?.unregisterAdapterDataObserver(dataSetChangeObserver)
        } catch (e: Throwable) {
            logD("unregisterAdapterDataObserver fail: $e")
        }
        try {
            viewPager.unregisterOnPageChangeCallback(vpOnPageChangeCallback)
        } catch (e: Throwable) {
            logD("unregisterOnPageChangeCallback fail: $e")
        }
        logD("detached")
    }

    override fun selectTab(position: Int) {
        if (position < 0) {
            return
        }
        if (interceptTabSelected(position, viewPager.currentItem)) {
            return
        }
        viewPager.setCurrentItem(position, false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun updateViewAt(position: Int, doUpdate: VB.(Boolean) -> Unit) {
        val holder =
            recyclerView.findViewHolderForAdapterPosition(position) as? BaseRvTabLayoutMediator<VB>.RvTabLayoutAdapter.Holder
        holder?.binding?.doUpdate(position == viewPager.currentItem)
    }

    override fun notifyItemChanged(position: Int, payload: Any?) {
        rvAdapter.notifyItemChanged(position, payload)
    }

    private fun interceptTabSelected(position: Int, lastPosition: Int): Boolean {
        return interceptTabSelected(this, position = position, lastPosition = lastPosition)
    }

    private fun refreshRvAdapter() {
        val size = vpAdapter?.itemCount ?: 0
        logD("pager data changed, newSize: $size")
        rvAdapter.submitList(List(size) { it }) {
            rvAdapter.updateSelect(viewPager.currentItem)
            interceptTabSelected(viewPager.currentItem, viewPager.currentItem)
            notifyVisibility()
        }
    }

    private fun notifyVisibility() {
        if (!listenItemFullVisible) {
            return
        }
        val manager = recyclerView.layoutManager as? LinearLayoutManager? ?: return
        val firstVisiblePos = manager.findFirstCompletelyVisibleItemPosition()
        val firstVisible = firstVisiblePos <= 0
        val lastVisiblePos = manager.findLastCompletelyVisibleItemPosition()
        val lastVisible = lastVisiblePos < 0 || lastVisiblePos >= rvAdapter.itemCount - 1
        logD("notify visibility, size: ${rvAdapter.itemCount}, top: $firstVisible($firstVisiblePos) bottom: $lastVisible($lastVisiblePos)")
        onItemFullVisible(firstVisible, lastVisible)
    }

    private fun RecyclerView.addScrollListener() {
        removeScrollListener()
        if (listenItemFullVisible) {
            addOnScrollListener(onScrollListener)
        }
    }

    private fun RecyclerView.removeScrollListener() {
        runCatching {
            removeOnScrollListener(onScrollListener)
        }
    }

    internal inner class RvTabLayoutAdapter : ListAdapter<Any, RvTabLayoutAdapter.Holder>(
        object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = true
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = false
        }
    ) {
        private var currentSelected = -1

        fun updateSelect(position: Int) {
            if (currentSelected == position) {
                return
            }
            logD("update select from $currentSelected to $position")
            val last = currentSelected
            currentSelected = position
            if (last in 0 until itemCount) {
                notifyItemChanged(last, "PAYLOAD_SELECTED_CHANGED")
            }
            notifyItemChanged(position, "PAYLOAD_SELECTED_CHANGED")
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): Holder = Holder(onCreateView(parent)).also {
            it.itemView.setOnClickListener { v ->
                val position = (v.tag as? Int?) ?: return@setOnClickListener
                selectTab(position)
            }
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            onBindViewHolder(holder, position, emptyList())
        }

        override fun onBindViewHolder(holder: Holder, position: Int, payloads: List<Any>) {
            val new = mutableSetOf<Any>()
            payloads.forEach {
                if (it is Collection<*>) {
                    new.addAll(it.filterNotNull())
                } else {
                    new.add(it)
                }
            }
            holder.bind(position, new)
        }

        internal inner class Holder(
            val binding: VB
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(position: Int, payloads: Set<Any>) {
                itemView.tag = position
                val isSelected = position == currentSelected
                itemView.isSelected = position == currentSelected
                //logD("onBindView, position: $position, selected: $isSelected, full bind: ${payloads.isEmpty()}")
                binding.onBindView(position, isSelected, payloads)
            }
        }

    }

    private class DataSetChangeObserver(
        private val onChanged: () -> Unit
    ) : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            onChanged.invoke()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onChanged()
        }

    }

    private fun logD(msg: String) {
        Log.d(TAG, msg)
    }

}