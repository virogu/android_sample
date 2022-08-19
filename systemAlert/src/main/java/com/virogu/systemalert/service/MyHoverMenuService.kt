package com.virogu.systemalert.service

import android.content.Context
import android.content.Intent
import com.virogu.systemalert.hover.MyHoverMenu
import io.mattcarroll.hover.HoverView
import io.mattcarroll.hover.window.HoverMenuService

/**
 * @author Virogu
 * @since 2022-08-19 10:24
 **/
class MyHoverMenuService : HoverMenuService() {

    companion object {
        fun startService(context: Context) {
            val intent = Intent(context, MyHoverMenuService::class.java)
            context.startService(intent)
        }
    }

    override fun onHoverMenuLaunched(intent: Intent, hoverView: HoverView) {
        val menu = MyHoverMenu(this)
        hoverView.setMenu(menu)
        hoverView.collapse()
    }


}