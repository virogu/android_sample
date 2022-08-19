package com.example.emptyapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        Timber.i("MyService 11111111111")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

}