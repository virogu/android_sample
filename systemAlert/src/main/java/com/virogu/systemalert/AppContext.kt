package com.virogu.systemalert

import android.app.Application
import timber.log.Timber

/**
 * Created by Virogu
 * Date 2022-08-18 10:36
 **/

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}