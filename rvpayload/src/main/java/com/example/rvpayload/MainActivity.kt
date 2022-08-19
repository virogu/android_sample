package com.example.rvpayload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rvpayload.databinding.ActivityMainBinding
import com.example.rvpayload.databinding.ListItemBinding

data class Person(
    val name: String,
    val gender: String,
    val age: Int,
)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        binding.init()
    }

    private fun ActivityMainBinding.init() {
        adapter = MyAdapter()
        val list1 = listOf(
            Person("张三", "男", 20),
            Person("李四", "男", 20),
            Person("王五", "男", 20),
        )
        val list2 = listOf(
            Person("张三", "男", 20),
            Person("李四", "男", 24),
            Person("王五", "男", 20),
        )
        var aTemTag = false

        listView.layoutManager = LinearLayoutManager(this@MainActivity)
        listView.adapter = adapter

        adapter.submitList(list1)

        button.setOnClickListener {
            if (aTemTag) {
                adapter.submitList(list1)
            } else {
                adapter.submitList(list2)
            }
            aTemTag = !aTemTag
        }
    }
}

class MyAdapter : ListAdapter<Person, MyAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        const val CHANGED_NAME = "CHANGED_NAME"
        const val CHANGED_GENDER = "CHANGED_GENDER"
        const val CHANGED_AGE = "CHANGED_AGE"

        val CHANGED_ALL = setOf(
            CHANGED_NAME,
            CHANGED_AGE,
            CHANGED_GENDER
        )

        private val COMPARATOR = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(
                oldItem: Person,
                newItem: Person
            ): Boolean {
                return true
            }

            override fun areContentsTheSame(
                oldItem: Person,
                newItem: Person
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: Person,
                newItem: Person
            ): Any {
                //payload是可以返回任意类型的值的，因为只是标记作用，在bindView时根据这个来判断需要刷新哪一部分控件
                //所以返回set数组是最方便的
                return setOfNotNull(
                    CHANGED_NAME.takeIf {
                        oldItem.name != newItem.name
                    },
                    CHANGED_GENDER.takeIf {
                        oldItem.gender != newItem.gender
                    },
                    CHANGED_AGE.takeIf {
                        oldItem.age != newItem.age
                    },
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).run {
            ViewHolder(this.root, this)
        }
    }

    private fun getData(position: Int) = if (position >= itemCount) {
        null
    } else {
        getItem(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 没有 Payload 的 onBindViewHolder 就是刷新全部控件
        holder.bindView(position, getData(position), CHANGED_ALL)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        //super.onBindViewHolder(holder, position, payloads)
        //super的话走的就是上面的全部刷新了

        //部分刷新时会进到这里
        //payloads是一个列表，我们曲出之前存进去的Set数组就行了，如果没有我们之前根据判断存进去的Set数组也是代表全刷新
        val payload = payloads.takeIf {
            it.isNotEmpty()
        }?.mapNotNull {
            if (it is Set<*>) {
                // 是Set数组，就要它了
                it
            } else {
                null
            }
        }?.flatten()?.toSet() ?: CHANGED_ALL
        holder.bindView(position, getData(position), payload)
    }

    inner class ViewHolder(
        itemView: View,
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            position: Int,
            data: Person?,
            payloads: Set<*>
        ) {
            data?.apply {
                binding.bind(position, data, payloads)
            }
        }

        private fun ListItemBinding.bind(
            position: Int,
            data: Person,
            payloads: Set<*>
        ) {
            // 平常bindView时都是直接设置控件的值或状态, 刷新时整个Item都会闪烁一下
            // 这里根据payloads就可以明确的知道只需要刷新那些控件就行了
            if (payloads.contains(CHANGED_NAME)) {
                tvName.text = data.name
            }
            if (payloads.contains(CHANGED_GENDER)) {
                tvGender.text = data.gender
            }
            if (payloads.contains(CHANGED_AGE)) {
                tvAge.text = "${data.age}"
            }
        }
    }
}