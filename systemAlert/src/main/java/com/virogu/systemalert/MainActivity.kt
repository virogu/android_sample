package com.virogu.systemalert

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.virogu.library.common.extention.singleToast
import com.virogu.library.common.extention.toast
import com.virogu.library.common.util.requestPermission
import com.virogu.systemalert.service.AlertService.Companion.startAlertService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeat(10) {
                singleToast("Test$it")
                delay(200)
            }
        }
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