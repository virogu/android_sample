package com.virogu.systemalert.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.virogu.library.common.extention.toast
import com.virogu.systemalert.service.AlertService.Companion.startAlertService

/**
 * @author Virogu
 * @since 2022-08-19 18:55
 **/
class BootReceiver : BroadcastReceiver() {
    @SuppressLint("LogNotTimber")
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BootReceiver", "on receive: ${intent.action}")
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                context.toast("已自启")
                context.startAlertService()
            }
            else -> {

            }
        }
    }
}