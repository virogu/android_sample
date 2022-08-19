package com.example.aidltest

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.widget.Toast


class MyIntentService : IntentService("MyIntentService") {
    private val serviceAidlInterface = object : ServiceAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {

        }

        override fun getValue(): Int {
            return 1111
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return serviceAidlInterface.asBinder()
    }

    override fun onHandleIntent(intent: Intent?) {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val binder: RequestBinder? = intent!!.getParcelableExtra("RequestBinder")
        binder?.apply {
            val value = this.clientAidlInterface.fileLiveDatas
            Toast.makeText(this@MyIntentService, "请求结果$value", Toast.LENGTH_SHORT).show()
        }

        return super.onStartCommand(intent, flags, startId)
    }


}