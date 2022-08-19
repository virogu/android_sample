package com.myserialport

import android_serialport_api.SerialPort
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.*

class SerialPortUtil(private val serialPortPath: String) {
    private val TAG = "SerialPortUtil"
    private var serialPort: SerialPort? = null
    private var inputStream: BufferedInputStream? = null
    private var outputStream: OutputStream? = null
    private var isStart = false

    /**
     * 打开串口，接收数据
     * 通过串口，接收单片机发送来的数据
     */
    fun openSerialPort(): Boolean {
        return try {
            serialPort = SerialPort(File(serialPortPath), 9600, 0)
            //调用对象SerialPort方法，获取串口中"读和写"的数据流
            inputStream = serialPort!!.inputStream
            outputStream = serialPort!!.outputStream
            isStart = true
            //getSerialPort();
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 关闭串口
     * 关闭串口中的输入输出流
     */
    fun closeSerialPort() {
        Timber.i("关闭串口")
        try {
            inputStream?.use {
                it.close()
            }
            outputStream?.use {
                it.close()
            }
            serialPort?.close()
            isStart = false
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            serialPort = null
            inputStream = null
            outputStream = null
        }
    }

    /**
     * 发送数据
     * 通过串口，发送数据到单片机
     *
     * @param data 要发送的数据
     */
    fun sendSerialPort(data: String): Boolean {
        return try {
            if (outputStream == null) {
                Timber.w("outputStream is null!")
                return false
            }
            val sendData = DataUtils.HexToByteArr(data)
            outputStream!!.write(sendData)
            outputStream!!.flush()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getSerialPortData(): String? {
        inputStream?.apply {
            val readData = ByteArray(1024)
            try {
                mark(0)
                val size = read(readData)
                reset()
                if (size > 0) {
                    return DataUtils.ByteArrToHex(readData, 0, size)
                }
            } catch (e: IOException) {
                Timber.d(e)
                e.printStackTrace()
            }
        }
        return null
    }

    companion object {
        /**
         * FE 00 01 00 00 EA 60 4B
         * FE   00 01     00 00 EA 60      4B
         * 头部  协议头     ACC延时时间 ms    FE后六位之和
         */
        @JvmStatic
        fun parseAccSendData(value: Int): String {
            var result = ""
            try {
                val hex = String.format("%08x", value).uppercase(Locale.ROOT)
                var checkDec = 0
                checkDec += 1
                val charArray = hex.toCharArray()
                var i = 0
                while (i < charArray.size) {
                    val temp = charArray[i].toString() + charArray[i + 1]
                    checkDec += temp.toInt(16)
                    i += 2
                }
                var checkHex = String.format("%02x", checkDec).uppercase(Locale.ROOT)
                if (checkHex.length > 2) {
                    checkHex = checkHex.substring(checkHex.length - 2)
                }
                result = "FE0001$hex$checkHex"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}