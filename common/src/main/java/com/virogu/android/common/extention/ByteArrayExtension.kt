@file:kotlin.jvm.JvmName("ByteArrayKt")

package com.virogu.android.common.extention

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.SystemClock
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.system.measureTimeMillis

private const val TAG = "ByteArrayEx"
/**
 * @Author Virogu
 * @Date 2021-12-06 下午 6:31:07
 */

/**
 * @param format    ByteArray数据格式，只支持 { ImageFormat.NV21、ImageFormat.YUY2 }
 * @param width     图片宽
 * @param height    图片高
 * @param targetFilePath 图片保存路径
 * @param quality   初始压缩比例
 * @param targetKb  目标文件大小，默认为0表示不压缩
 */
@SuppressLint("LogNotTimber")
fun ByteArray.saveToFile(
    format: Int,
    width: Int,
    height: Int,
    targetFilePath: String,
    quality: Int = 80,
    targetKb: Int = 0
): Boolean = try {
    val image = YuvImage(this, format, width, height, null)
    val rect = Rect(0, 0, width, height)
    var count = 0
    val millis = measureTimeMillis {
        FileOutputStream(targetFilePath).use { f ->
            if (targetKb > 0) {
                ByteArrayOutputStream().use {
                    var quality1 = quality
                    do {
                        count++
                        it.reset()
                        image.compressToJpeg(rect, quality1, it)
                        quality1 -= 10
                    } while (quality1 > 10 && it.size() > (targetKb * 1024))
                    f.write(it.toByteArray())
                }
            } else {
                count = 1
                image.compressToJpeg(rect, quality, f)
            }
        }
    }
    Log.v("FilesKt", "compress file[$targetFilePath] $count times, spend $millis ms")
    true
} catch (e: Throwable) {
    false
}

fun ByteArray.saveToFile(
    format: Int,
    width: Int,
    height: Int,
    targetFile: File,
    quality: Int = 80,
    targetKb: Int = 0
): Boolean = saveToFile(format, width, height, targetFile.path, quality, targetKb)


fun File.readYUV(): Pair<ByteArray, Pair<Int, Int>>? {
    return try {
        val begin = SystemClock.elapsedRealtime()
        val option = BitmapFactory.Options()
        //option.inSampleSize = 4
        val bitmap = BitmapFactory.decodeFile(this.absolutePath, option)
        val inputWidth = bitmap.width
        val inputHeight = bitmap.height
        val argb = IntArray(inputWidth * inputHeight)
        // Bitmap 获取 argb
        bitmap.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight)
        val yuv = ByteArray(inputWidth * inputHeight * 3 / 2)
        // argb 8888 转 i420
        convertArgbToI420(argb, yuv, inputWidth, inputHeight)
        bitmap.recycle()
        Log.d(
            TAG,
            "read yuv data form [${this.path}] spend ${SystemClock.elapsedRealtime() - begin}ms"
        )
        Pair(yuv, Pair(inputWidth, inputHeight))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// argb 8888 转 i420
fun convertArgbToI420(argb: IntArray, i420: ByteArray, width: Int, height: Int) {
    val frameSize = width * height
    var yIndex = 0 // Y start index
    var uIndex = frameSize // U statt index
    var vIndex = frameSize * 5 / 4 // V start index: w*h*5/4
    var a: Int
    var R: Int
    var G: Int
    var B: Int
    var Y: Int
    var U: Int
    var V: Int
    var index = 0
    for (j in 0 until height) {
        for (i in 0 until width) {
            a = argb[index] and -0x1000000 shr 24 //  is not used obviously
            R = argb[index] and 0xff0000 shr 16
            G = argb[index] and 0xff00 shr 8
            B = argb[index] and 0xff shr 0

            // well known RGB to YUV algorithm
            Y = (66 * R + 129 * G + 25 * B + 128 shr 8) + 16
            U = (-38 * R - 74 * G + 112 * B + 128 shr 8) + 128
            V = (112 * R - 94 * G - 18 * B + 128 shr 8) + 128

            // I420(YUV420p) -> YYYYYYYY UU VV
            i420[yIndex++] = (if (Y < 0) 0 else if (Y > 255) 255 else Y).toByte()
            if (j % 2 == 0 && i % 2 == 0) {
                i420[uIndex++] = (if (U < 0) 0 else if (U > 255) 255 else U).toByte()
                i420[vIndex++] = (if (V < 0) 0 else if (V > 255) 255 else V).toByte()
            }
            index++
        }
    }
}