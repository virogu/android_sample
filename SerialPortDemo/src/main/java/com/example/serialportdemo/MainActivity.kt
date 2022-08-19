package com.example.serialportdemo

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.serialportdemo.databinding.ActivityMainBinding
import com.myserialport.SerialPortUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var serialPortUtil: SerialPortUtil? = null
    private var job: Job? = null
    private val dataFormat = SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS:", Locale.getDefault())
    private val portData = MutableSharedFlow<String?>(0, 5, BufferOverflow.DROP_OLDEST)
    private lateinit var pref: SharedPreferences

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        Timber.plant(Timber.DebugTree())
        binding.initData()
        binding.initClicker()
    }

    private fun ActivityMainBinding.initData() {
        lifecycleScope.launch(Dispatchers.Main) {
            portData.collect {
                if (it == null) {
                    tvPortDataTime.text = ""
                    tvPortData.text = ""
                } else {
                    tvPortDataTime.text = dataFormat.format(Date())
                    tvPortData.text = it
                }
            }
        }
    }

    private fun ActivityMainBinding.initClicker() {
        val lastPortPath = pref.getString("last_port_path", "/sys/bus/i2c/devices/1-0010/status")
        etPortUrl.setText(lastPortPath)
        etPortUrl.doAfterTextChanged {
            it?.toString()?.apply {
                pref.edit().putString("last_port_path", this).apply()
            }
        }

        btOpenPort.setOnClickListener {
            it.isEnabled = false
            openPort()
            it.isEnabled = true
        }
        btClosePort.setOnClickListener {
            stopReadJob()
            serialPortUtil?.apply {
                closeSerialPort()
                serialPortUtil = null
                "关闭串口成功".show()
            }
        }

        btSendData.setOnClickListener {
            //et_send_data?.text?.toString()?.apply {
            //    serialPortUtil?.sendSerialPort(data = this)
            //}
        }
    }

    private fun openPort() {
        stopReadJob()
        serialPortUtil?.closeSerialPort()
        serialPortUtil = null

        val path = binding.etPortUrl.text.toString()
        if (path.startsWith("/dev")) {
            serialPortUtil = SerialPortUtil(path)
            serialPortUtil?.also { port ->
                with(port.openSerialPort()) {
                    if (this) {
                        startReadJob()
                        "打开串口成功"
                    } else {
                        serialPortUtil?.closeSerialPort()
                        serialPortUtil = null
                        "打开串口失败"
                    }
                }.show()
            }
        } else {
            val f = File(path)
            //val test = File("/sdcard/test")
            //if(!test.exists()){
            //    test.createNewFile()
            //}
            if (f.exists() && f.canRead()) {
                job = lifecycleScope.launch(Dispatchers.IO) {
                    delay(2000)
                    while (isActive) {
                        f.readText().apply {
                            Timber.v("read result: $this")
                            portData.emit("data: $this")
                        }
                        //test.outputStream().use {
                        //    it.write("test".toByteArray())
                        //}
                        //Timber.v("test time: ${test.lastModified()}")
                        delay(1000)
                    }
                }
                "打开串口成功".show()
            } else {
                "打开串口失败".show()
            }
        }
    }

    private fun startReadJob() {
        job?.cancel()
        job = lifecycleScope.launch(Dispatchers.IO) {
            while (isActive) {
                serialPortUtil?.getSerialPortData().also { data ->
                    portData.emit(data)
                }
                delay(100L)
            }
        }
    }

    private fun stopReadJob() {
        job?.cancel()
        portData.tryEmit(null)
    }

    private fun String.show() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        stopReadJob()
        serialPortUtil?.closeSerialPort()
        serialPortUtil = null
        super.onDestroy()
    }

}