package com.example.testapp.timber

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.testapp.BuildConfig
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * @Author Virogu
 * @Date 2021-12-07 下午 12:08:52
 */

@SuppressLint("LogNotTimber")
class LogTree(
    private val context: Context,
    private val level: Int = Log.INFO
) : Timber.DebugTree() {

    private val simpleFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    private val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val logPath by lazy {
        val f = context.applicationContext.getExternalFilesDir("logs")
        Log.i("Log", "Log path: $f")
        f
    }

    private val versionName by lazy {
        BuildConfig.VERSION_NAME
    }

    private val logFileMap: HashMap<String, FileOutputStream> = HashMap()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        if (priority < level) {
            return
        }
        val file = File(logPath, "log_${versionName}_${simpleDateFormatter.format(Date())}.txt")
        if (!file.exists()) {
            try {
                file.parentFile?.mkdirs()
                file.createNewFile()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        val level = when (priority) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> return
        }
        val s = StringBuilder().apply {
            append("[$level]")
            append(" [${simpleFormatter.format(Date())}]")
            tag?.also {
                append(" [$it]")
            }
            append(" $message")
            t?.also {
                append("\n$t")
            }
            append("\n")
        }.toString()
        val outputStream = synchronized(logFileMap) {
            logFileMap[file.name] ?: FileOutputStream(file, true).also {
                logFileMap[file.name] = it
            }
        }
        outputStream.write(s.toByteArray())
    }
}