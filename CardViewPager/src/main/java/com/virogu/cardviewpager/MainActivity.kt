package com.virogu.cardviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    private var list: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..10) {
            list.add(R.drawable.header)
        }
        findViewById<ViewPager2>(R.id.viewPager).apply {
            adapter = VpAdapter(
                listItem = list
            )
            offscreenPageLimit = 3
            setPageTransformer(
                CardPagerTransform(
                    viewPager = this,
                    scrollRotation = 10
                )
            )
        }
    }

    internal inner class VpAdapter(private var listItem: List<Int>) :
        RecyclerView.Adapter<VpAdapter.PagerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            return PagerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image, parent, false)
            )
        }

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            holder.bindView(listItem[position], position)
        }

        override fun getItemCount(): Int {
            return listItem.size
        }

        inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val img: ImageView = itemView.findViewById(R.id.imageView)
            private val textView: TextView = itemView.findViewById(R.id.textView)
            fun bindView(id: Int, position: Int) {
                img.setImageResource(id)
                val t = "${position + 1}"
                textView.text = t
            }
        }


    }
}