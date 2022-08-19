package com.virogu.xlog

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import timber.log.Timber
import java.io.File

/**
 * @author Virogu
 * @since 2022-04-15 17:07
 *
 * @sample
 *  Timber配合xlog记录日志文件
 *
 *  ```
 *  //使用前需引用以下两个库
 *  implementation 'com.jakewharton.timber:timber:5.0.1'
 *  implementation 'com.tencent.mars:mars-xlog:1.2.6'
 *  ```
 *
 *  ```
 *  //单进程程序初始化示例：
 *  class AppContext : Application() {
 *      ... ...
 *      override fun onCreate() {
 *          XTimber.initXLog(this)
 *          Timber.plant(XTimber.Tree(BuildConfig.DEBUG))
 *      }
 *      ... ...
 *  }
 *  ```
 *
 * ```
 *  //多进程程序初始化示例：
 *  class AppContext : Application() {
 *      ... ...
 *      override fun onCreate() {
 *          //获取当前进程名
 *          val processName = getProcessName(this)
 *          //这里以进程名称区分日志文件
 *          XTimber.initXLog(this, namePrefix = "$processName")
 *          when (processName) {
 *              "com.example.emptyapp" -> {
 *                  //Timber plant只需要在主进程执行一次
 *                  Timber.plant(XTimber.Tree(BuildConfig.DEBUG))
 *              }
 *              else -> {}
 *          }
 *      }
 *      ... ...
 *  }
 * ```
 *
 * ```
 *  //日志打印时使用Timber
 *  Timber.v("Log")
 *  Timber.d("Log")
 *  Timber.i("Log")
 *  Timber.w("Log")
 *  Timber.e("Log")
 *  Timber.e(IllegalArgumentException("test error"))
 *  Timber.e(IllegalArgumentException("test error"), "error with message")
 * ```
 *
 * ```
 *  //如果项目引用了其他包含libc++_shared的库，
 *  //编译时可能会有 libc++_shared.so 库冲突的问题
 *  //需要在 app模块 下面的 build.gradle 里面加上这个：
 *  android {
 *      ...
 *      packagingOptions {
 *          pickFirst 'lib/x86/libc++_shared.so'
 *          pickFirst 'lib/x86_64/libc++_shared.so'
 *          pickFirst 'lib/armeabi-v7a/libc++_shared.so'
 *          pickFirst 'lib/arm64-v8a/libc++_shared.so'
 *      }
 *      ...
 *  }
 * ```
 * ```
 *  //kts写法
 *  android {
 *      ...
 *      packagingOptions {
 *          jniLibs.pickFirsts.add("lib/x86/libc++_shared.so")
 *          jniLibs.pickFirsts.add("lib/x86_64/libc++_shared.so")
 *          jniLibs.pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
 *          jniLibs.pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
 *      }
 *      ...
 *  }
 * ```
 *
 */
class XTimber {

    companion object {

        init {
            System.loadLibrary("c++_shared")
            System.loadLibrary("marsxlog")
        }

        /***
         * 初始化xLog
         * @param context Context
         * @param cachePath log缓存目录 默认是 Android/data/packageName/files/xlog/cache
         * @param logPath log日志目录 默认是 Android/data/packageName/files/xlog
         * @param namePrefix log日志文件前缀 默认是packageName，最终文件名格式为 “namePrefix_yyyyMMdd.xlog"
         */
        @SuppressLint("LogNotTimber")
        @Synchronized
        fun initXLog(
            context: Context,
            cachePath: String? = null,
            logPath: String? = null,
            namePrefix: String? = null,
        ) {
            val mLogPath: String = logPath
                ?: context.applicationContext.getExternalFilesDir("xlog")?.path
                ?: File(context.applicationContext.filesDir, "xlog").path

            File(mLogPath).apply {
                if (!this.exists()) {
                    this.mkdirs()
                }
            }

            val mCachePath: String = cachePath
                ?: context.applicationContext.getExternalFilesDir("xlog/cache")?.path
                ?: File(context.applicationContext.filesDir, "xlog/cache").path

            File(mCachePath).apply {
                if (!this.exists()) {
                    this.mkdirs()
                }
            }

            val mLogFileName: String = namePrefix ?: context.applicationContext.packageName

            val xlog = Xlog()
            Log.setLogImp(xlog)
            Log.setConsoleLogOpen(false)
            Log.appenderOpen(
                Xlog.LEVEL_INFO,
                Xlog.AppednerModeSync,
                mCachePath,
                mLogPath,
                mLogFileName.replace(":", "_"),
                0
            )
            android.util.Log.w(
                "XTimber",
                "XLog initialized,\n cachePath [$mCachePath],\n logPath [$mLogPath],\n logFileName [$mLogFileName]"
            )
        }

        /***
         * 反初始化xLog
         */
        @Synchronized
        fun uninitialized() {
            Log.appenderClose()
        }
    }

    /**
     * Timber Tree
     * @param isDebug 是否是Debug环境，不是Debug环境时，只显示INFO以上级别的日志
     */
    class Tree(private val isDebug: Boolean) : Timber.DebugTree() {

        @SuppressLint("LogNotTimber")
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // 不是Debug模式，只显示INFO以上级别的日志
            if (!isDebug && priority < android.util.Log.INFO) {
                return
            }
            if (message.length < MAX_LOG_LENGTH) {
                if (priority == android.util.Log.ASSERT) {
                    android.util.Log.wtf(tag, message)
                } else {
                    android.util.Log.println(priority, tag, message)
                }
                xLogRecord(priority, tag, message)
                return
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length = message.length
            while (i < length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == android.util.Log.ASSERT) {
                        android.util.Log.wtf(tag, part)
                    } else {
                        android.util.Log.println(priority, tag, part)
                    }
                    xLogRecord(priority, tag, part)
                    i = end
                } while (i < newline)
                i++
            }
        }

        private fun xLogRecord(priority: Int, tag: String?, msg: String) {
            when (priority) {
                android.util.Log.VERBOSE -> {
                    Log.v(tag, msg)
                }
                android.util.Log.DEBUG -> {
                    Log.d(tag, msg)
                }
                android.util.Log.INFO -> {
                    Log.i(tag, msg)
                }
                android.util.Log.WARN -> {
                    Log.w(tag, msg)
                }
                android.util.Log.ERROR -> {
                    Log.e(tag, msg)
                }
                android.util.Log.ASSERT -> {
                    Log.f(tag, msg)
                }
                else -> {

                }
            }
        }

        companion object {
            private const val MAX_LOG_LENGTH = 4000
        }

    }

}

