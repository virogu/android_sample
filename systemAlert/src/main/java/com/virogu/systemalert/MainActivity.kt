package com.virogu.systemalert

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.virogu.library.common.extention.toast
import com.virogu.library.common.util.requestPermission
import com.virogu.systemalert.service.AlertService.Companion.startAlertService

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(
            listOf(Manifest.permission.SYSTEM_ALERT_WINDOW)
        ).request { allGrant, _, _ ->
            if (allGrant) {
                application.startAlertService()
                //MyHoverMenuService.startService(applicationContext)
            } else {
                toast("请授权所需权限")
            }
            finish()
        }
    }

}