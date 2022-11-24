package com.example.testapp.chart

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.testapp.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


/**
 * @author Virogu
 * @since 2022-11-23 16:17
 **/

class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    constructor(context: Context) : this(context, R.layout.custom_marker_view)

    private val textView: TextView? = findViewById<View>(android.R.id.text1) as? TextView?

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        textView?.apply {
            when (e) {
                is SignsData -> {
                    text = "时间: ${e.time}\n${e.name}: ${e.value}${e.unit}"
                }
                else -> {}
            }
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}
