package com.virogu.systemalert.hover

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import io.mattcarroll.hover.Content

/**
 * @author Virogu
 * @since 2022-08-19 11:26
 **/
class MyContent(
    private val context: Context,
    private val text: String
) : Content {

    override fun getView(): View {
        val v = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null)
        initView(v)
        return v
    }

    private fun initView(view: View) {
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = text
    }

    override fun isFullscreen(): Boolean {
        return false
    }

    override fun onShown() {
    }

    override fun onHidden() {
    }
}