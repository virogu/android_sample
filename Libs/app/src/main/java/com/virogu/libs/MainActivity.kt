package com.virogu.libs

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.virogu.library.permission.util.PermissionUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        PermissionUtil.requestPermission(
            this,
            getString(R.string.chat_permission_tips),
            permissions,
            resources.getColor(R.color.colorPrimary),
            resources.getColor(R.color.colorPrimaryDark)
        ).request { allGranted, _, _ ->
            if (!allGranted) {
                Toast.makeText(
                    this,
                    getString(R.string.denied_permission_tips),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}