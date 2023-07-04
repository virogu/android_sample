package androidx.recyclerview.widget

import androidx.viewbinding.ViewBinding

/**
 * Created by Virogu
 * Date 2023/06/30 上午 10:14:28
 **/
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