package com.virogu.customview.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
/**
 * @author Virogu
 * @since 2020/12/10
 */
class LogView : RecyclerView {

    private var mManager = LinearLayoutManager(context)
    private var logList: MutableList<LogObj> = ArrayList()
    private var autoScroll = true
    private var format = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
    private var maxLogSize = 10000
    private var splitString = " -> "
    private var autoWrap = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        val RED = Color.parseColor("#F44336")
        val GREEN = Color.parseColor("#3B983F")
        val ORANGE = Color.parseColor("#C69709")
        val GRAY = Color.parseColor("#3A3A3A")
    }

    fun log(message: String, @ColorInt color: Int = GRAY) {
        var newLog = "${format.format(System.currentTimeMillis())}$splitString$message"
        if(autoWrap) newLog += "\n"
        logList.add(LogObj(newLog, color))
        if (logList.size > maxLogSize && logList.size > 1) {
            do {
                logList.removeFirst()
            } while (logList.size > maxLogSize)
        }
        this.adapter?.apply {
            post {
                notifyItemRangeInserted(itemCount, 1)
                notifyItemChanged(itemCount - 1)
                if (autoScroll) {
                    scrollToPosition(itemCount - 1)
                }
            }
        }
    }

    fun logI(message: String) {
        log(message, GREEN)
    }

    fun logW(message: String) {
        log(message, ORANGE)
    }

    fun logE(message: String) {
        log(message, RED)
    }

    fun reverseLayout(reverse: Boolean) {
        mManager.reverseLayout = reverse
        this.layoutManager = mManager
    }

    fun clearLog() {
        logList.clear()
        post {
            adapter?.notifyDataSetChanged()
        }
    }

    fun setMaxLogSize(size: Int) {
        this.maxLogSize = size
    }

    fun setSplitString(split: String) {
        this.splitString = split
    }

    fun setDataFormat(format: SimpleDateFormat) {
        this.format = format
    }

    fun setAutoWrap(enable: Boolean) {
        this.autoWrap = enable
    }

    init {
        mManager.reverseLayout = true
        this.layoutManager = mManager
        this.adapter = object : RecyclerView.Adapter<LogHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
                return LogHolder(
                    LayoutInflater.from(context.applicationContext)
                        .inflate(android.R.layout.simple_list_item_1, null)
                )
            }

            override fun onBindViewHolder(holder: LogHolder, position: Int) {
                holder.textView.text = logList[position].log
                holder.textView.setTextColor(logList[position].color)
            }

            override fun getItemCount(): Int {
                return logList.size
            }

        }

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val manager = recyclerView.layoutManager as LinearLayoutManager
                    val pos = manager.findLastVisibleItemPosition()
                    autoScroll = pos >= logList.size - 2
                }
            }
        })
    }

    internal class LogHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }

    data class LogObj(val log: String, @ColorInt val color: Int)
}