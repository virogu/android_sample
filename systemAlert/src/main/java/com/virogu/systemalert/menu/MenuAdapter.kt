package com.virogu.systemalert.menu

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.virogu.systemalert.databinding.FloatMenuItemBinding

data class FloatMenu(
    val name: String,
    val onClick: (menu: FloatMenu, Context) -> Unit = { _, _ -> }
)

class FloatMenuAdapter : RecyclerView.Adapter<FloatMenuAdapter.ViewHolder>() {

    private var menuList: List<FloatMenu> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(menuList: List<FloatMenu>) {
        this.menuList = menuList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FloatMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also {
            it.itemView.setOnClickListener { v ->
                val menu = (v.tag as? Int?)?.let { position ->
                    menuList.getOrNull(position)
                } ?: return@setOnClickListener
                menu.onClick(menu, v.context)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, menuList.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class ViewHolder(
        private val binding: FloatMenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, menu: FloatMenu?) {
            itemView.tag = position
            menu?.also {
                binding.bind(position, menu)
            }
        }

        private fun FloatMenuItemBinding.bind(position: Int, menu: FloatMenu) {
            tvTitle.text = menu.name
        }
    }

}