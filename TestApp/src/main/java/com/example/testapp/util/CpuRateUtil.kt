package com.example.testapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @Author Virogu
 * @Date 2021-07-19 下午 6:03
 */

object CpuRateUtil {

    suspend fun getRate(): Float = withContext(Dispatchers.IO) {
        val map1 = getMap() ?: return@withContext 0f //采样第一次CPU信息快照
        val totalTime1 =
            map1["user"]!!.toLong() + map1["nice"]!!.toLong() + map1["system"]!!
                .toLong() + map1["idle"]!!.toLong() + map1["iowait"]!!
                .toLong() + map1["irq"]!!
                .toLong() + map1["softirq"]!!.toLong() //获取totalTime1
        val idleTime1 = map1["idle"]!!.toLong() //获取idleTime1
        delay(300)
        val map2 = getMap() ?: return@withContext 0f  //采样第二次CPU快照
        val totalTime2 = map2["user"]!!.toLong() + map2["nice"]!!.toLong() + map2["system"]!!
            .toLong() + map2["idle"]!!.toLong() + map2["iowait"]!!
            .toLong() + map2["irq"]!!
            .toLong() + map2["softirq"]!!.toLong() //获取totalTime2
        val idleTime2 = map2["idle"]!!.toLong() //获取idleTime2
        return@withContext (100 * (totalTime2 - totalTime1 - (idleTime2 - idleTime1)) / (totalTime2 - totalTime1)).toFloat()
    }


    //采样CPU信息快照的函数，返回Map类型
    fun getMap(): Map<String, String>? {
        val cpuInfos: Array<String>?
        try {
            val br = BufferedReader(InputStreamReader(FileInputStream("/proc/stat"))) //读取CPU信息文件
            val load: String = br.readLine()
            br.close()
            cpuInfos = load.split(" ").toTypedArray()
            val map: MutableMap<String, String> = HashMap()
            map["user"] = cpuInfos[2]
            map["nice"] = cpuInfos[3]
            map["system"] = cpuInfos[4]
            map["idle"] = cpuInfos[5]
            map["iowait"] = cpuInfos[6]
            map["irq"] = cpuInfos[7]
            map["softirq"] = cpuInfos[8]
            Timber.d("cpu info: $map")
            return map
        } catch (e: Throwable) {
            Timber.w(e)
        }
        return null
    }
}