package com.example.recyclerselection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.HashSet

class MainActivity : AppCompatActivity(), ActionMode.Callback {

    private val adapter = MainAdapter()
    private var actionMode: ActionMode? = null
    private lateinit var tracker: SelectionTracker<Long>
    private var list = createRandomIntList()
    private var selectSet: MutableSet<Int> = HashSet()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        tracker = SelectionTracker.Builder(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val size = tracker.selection.size()
                if (size <= 0) {
                    actionMode?.finish()
                } else {
                    if (actionMode == null) {
                        actionMode = startActionMode(this@MainActivity)
                    }
                    actionMode?.title = "选择了${size}项"
                }
            }
        })
        adapter.tracker = tracker
        adapter.list = list
        adapter.notifyDataSetChanged()
    }

    private fun createRandomIntList(): List<Int> {
        val random = Random()
        return (1..50).map { random.nextInt() }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return if (actionMode == null) {
            actionMode = mode
            val inflater = mode?.menuInflater
            inflater?.inflate(R.menu.action_menu, menu)
            true
        } else {
            false
        }
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.select_all -> {
                if (tracker.selection.size() >= list.size) {
                    tracker.clearSelection()
                } else {
                    recyclerView.adapter?.apply {
                        for (i in 0 until itemCount) {
                            tracker.select(getItemId(i))
                        }
                    }
                }
            }
            R.id.select_ok -> {
                selectSet.clear()
                tracker.selection.forEach {
                    selectSet.add(it.toInt())
                }
                val size = tracker.selection.size()
                Toast.makeText(this, "选择了${size}项", Toast.LENGTH_SHORT).show()
                tracker.clearSelection()
            }
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        tracker.clearSelection()
    }
}

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as MainAdapter.ViewHolder).getItemDetails()
        }
        return null
    }
}

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    var list: List<Int> = arrayListOf()
    var tracker: SelectionTracker<Long>? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val number = list[position]
        tracker?.let {
            holder.bind(number, it.isSelected(position.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var text: TextView = view.findViewById(android.R.id.text1)

        fun bind(value: Int, isActivated: Boolean = false) {
            //设置个背景色 根据选中状态显示不同颜色
            text.setBackgroundResource(R.drawable.drawable)
            text.text = value.toString()
            itemView.isActivated = isActivated
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int {
                    return adapterPosition
                }

                override fun getSelectionKey(): Long {
                    return itemId
                }
            }
    }

    init {
        setHasStableIds(true)
    }
}