package com.example.aidltest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class UploadActivity : AppCompatActivity() {

    private lateinit var conn: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        conn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                Timber.i("UploadFilesIntentService onServiceConnected")
                val serviceAidlInterface = ServiceAidlInterface.Stub.asInterface(service)
                val value = serviceAidlInterface.value
                findViewById<TextView>(R.id.textView1).text = "$value"
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Timber.w("UploadFilesIntentService onServiceDisconnected")
            }
        }

        val intent = Intent(this, MyIntentService::class.java)
        intent.action = "com.example.MyIntentService"
        intent.setPackage("com.example")
        intent.putExtra("RequestBinder", RequestBinder())
        startService(intent)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }
}