package com.example.aidltest

import android.app.Application
import timber.log.Timber

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}