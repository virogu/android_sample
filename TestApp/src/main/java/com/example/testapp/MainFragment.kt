package com.example.testapp

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.bean.PagerBean
import com.example.testapp.databinding.FragmentMainBinding
import com.virogu.base.fragment.BaseBindingFragment

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun FragmentMainBinding.onViewCreated() {
        val itemAdapter = ItemAdapter()
        listItem.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            itemAdapter.setPagers(PagerBean.values().toList())
        }
    }

    internal inner class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        private var pagers: List<PagerBean> = emptyList()

        @SuppressLint("NotifyDataSetChanged")
        fun setPagers(pagers: List<PagerBean>) {
            this.pagers = pagers
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view).also {
                it.itemView.setOnClickListener { v ->
                    val position = v.tag as? Int? ?: return@setOnClickListener
                    val pager = pagers.getOrNull(position) ?: return@setOnClickListener
                    when (val target = pager.target()) {
                        is Fragment -> {
                            parentFragmentManager
                                .beginTransaction()
                                .replace(id, target)
                                .addToBackStack(null)
                                .commit()
                        }
                        is Class<*> -> {
                            runCatching {
                                val i = Intent(v.context, target)
                                startActivity(i)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindView(position, pagers[position])
        }

        override fun getItemCount() = pagers.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val text1: TextView = itemView.findViewById(android.R.id.text1)
            private val text2: TextView = itemView.findViewById(android.R.id.text2)

            fun bindView(position: Int, pager: PagerBean) {
                itemView.tag = position
                text1.text = pager.pager_name
                text2.text = pager.desc
            }

        }
    }


}