package com.example.emptyapp

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log
import com.virogu.xlog.XTimber
import timber.log.Timber


/**
 * Created by Virogu
 * Date 2022-04-02 11:35
 **/

class AppContext : Application() {

    @SuppressLint("LogNotTimber")
    override fun onCreate() {
        super.onCreate()
        //ProcessLifecycleOwner.get().lifecycle.addObserver()
        val processName = getProcessName(this)
        Log.i("AppContext", "AppContext on Create, processName: $processName")
        XTimber.initXLog(this, namePrefix = "$processName")
        when (processName) {
            "com.example.emptyapp" -> {
                // Timber plant只需要在主进程执行一次
                Timber.plant(XTimber.Tree(BuildConfig.DEBUG))
                init()
            }
            else -> {}
        }
    }

    private fun init() {

    }

    private fun getProcessName(context: Context): String? {
        val am: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
            ?: return null
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return null
    }

}