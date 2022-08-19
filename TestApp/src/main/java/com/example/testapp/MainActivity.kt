package com.example.testapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.navigation.NavigationRailActivity
import com.example.testapp.activity.SensorActivity
import com.example.testapp.fragment.ProgressBarFragment
import com.example.testapp.fragment.SystemInfoFragment
import com.example.testapp.navigation.BottomNavigationActivity
import com.example.testapp.navigation.NavigationActivity
import com.example.testapp.timber.LogTree
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var listView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(LogTree(this))
        listView = findViewById(R.id.list_item)
        adapter = ItemAdapter(this)
        listView.layoutManager = LinearLayoutManager(this)
        listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        listView.adapter = adapter
        adapter.setPagers(PagerBean.values().toList())
    }

    internal inner class ItemAdapter(private val context: Context) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        private var pagers: List<PagerBean> = emptyList()

        @SuppressLint("NotifyDataSetChanged")
        fun setPagers(pagers: List<PagerBean>) {
            this.pagers = pagers
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindView(pagers[position])
        }

        override fun getItemCount(): Int {
            return pagers.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val text1: TextView = itemView.findViewById(android.R.id.text1)
            private val text2: TextView = itemView.findViewById(android.R.id.text2)

            fun bindView(pager: PagerBean) {
                text1.text = pager.pager_name
                text2.text = pager.desc
                itemView.setOnClickListener {
                    onHandleClick(pager)
                }
            }

            private fun onHandleClick(pager: PagerBean) {
                if (supportFragmentManager.fragments.isNotEmpty()) {
                    return
                }
                if (pager.target is Fragment) {
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.frame_layout, pager.target)
                        .addToBackStack(null)
                        .commit()
                } else if (pager.target is Class<*>) {
                    val i = Intent(context, pager.target)
                    startActivity(i)
                }
            }
        }
    }


}

enum class PagerBean(
    val pager_name: String,
    val target: Any?,
    val desc: String = ""
) {
    ProgressBar("ProgressBar", ProgressBarFragment(), "ProgressBar"),
    SystemInfo("SystemInfo", SystemInfoFragment(), "SystemInfo"),
    GSensor("G-Sensor", SensorActivity::class.java, "G-Sensor"),
    NavigationRail(
        "NavigationRail",
        NavigationRailActivity::class.java,
        "a navigation rail demo"
    ),
    Navigation(
        "NavigationActivity",
        NavigationActivity::class.java,
        "a navigation activity demo"
    ),
    BottomNavigation(
        "BottomNavigationActivity",
        BottomNavigationActivity::class.java,
        "a bottom navigation activity demo"
    ),
}