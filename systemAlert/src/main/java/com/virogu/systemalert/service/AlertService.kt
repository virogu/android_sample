package com.virogu.systemalert.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.view.*
import androidx.core.view.children
import androidx.core.view.isVisible
import com.virogu.library.common.extention.singleToast
import com.virogu.systemalert.R
import com.virogu.systemalert.databinding.LayAlertBinding
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.min

class AlertService : Service() {

    private lateinit var binding: LayAlertBinding

    private val windowManager by lazy {
        application.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private val preferences by lazy {
        application.getSharedPreferences("alert_service", Context.MODE_PRIVATE)
    }

    private val layoutParams by lazy {
        getWindowLayoutParams()
    }

    companion object {
        fun Context.startAlertService() {
            val intent = Intent(this, AlertService::class.java)
            startService(intent)
        }
    }

    private val mBinder = object : Binder() {

    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        if (this::binding.isInitialized) {
            Timber.w("already initialized, remove old view")
            removeView()
        }
        binding = LayAlertBinding.inflate(LayoutInflater.from(application))
        binding.initView()
        addView()
    }

    override fun onDestroy() {
        removeView()
        super.onDestroy()
    }

    private fun addView() {
        windowManager.addView(binding.root, getWindowLayoutParams())
    }

    private fun removeView() {
        windowManager.removeView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun LayAlertBinding.initView() {
        layRoot.setOnTouchListener(onTouchListener)
        layContainer.children.forEachIndexed { index, view ->
            view.setOnClickListener {
                singleToast("点击了第${index + 1}个按钮")
                Timber.i("点击了第${index + 1}个按钮")
            }
        }
        imgExtend.setOnClickListener {
            layContainer.isVisible = !binding.layContainer.isVisible
            imgExtend.setImageResource(
                if (layContainer.isVisible) {
                    R.drawable.ic_baseline_keyboard_arrow_up
                } else {
                    R.drawable.ic_baseline_keyboard_arrow_down
                }
            )
        }
    }

    private val onTouchListener = object : View.OnTouchListener {
        private val shake = 10
        private var downX = 0f
        private var downY = 0f
        private var pair = Pair(0, 0)
        private var tag = 0

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Timber.d("ACTION_DOWN: [${event.x}, ${event.y}][${event.rawX},${event.rawY}][${layoutParams.x},${layoutParams.y}]")
                    downX = event.rawX
                    downY = event.rawY
                    pair = Pair(layoutParams.x, layoutParams.y)
                    tag = 1
                }
                MotionEvent.ACTION_MOVE -> {
                    if (tag == 0) {
                        downX = event.rawX
                        downY = event.rawY
                        pair = Pair(layoutParams.x, layoutParams.y)
                        tag = 1
                    }
                    val dx = (event.rawX - downX).toInt()
                    val dy = (event.rawY - downY).toInt()
                    Timber.v("ACTION_MOVE: [${event.x}, ${event.y}][${event.rawX},${event.rawY}][$dx,$dy]")
                    if (abs(dx) < shake && abs(dy) < shake) {
                        return true
                    }
                    val targetX = with(pair.first + dx) {
                        if (this < 0) {
                            0
                        } else {
                            min(this, event.rawX.toInt())
                        }
                    }
                    val targetY = with(pair.second + dy) {
                        if (this < 0) {
                            0
                        } else {
                            min(this, event.rawY.toInt())
                        }
                    }
                    layoutParams.x = targetX
                    layoutParams.y = targetY
                    windowManager.updateViewLayout(binding.root, layoutParams)
                }
                MotionEvent.ACTION_UP -> {
                    tag = 0
                    val dx = event.rawX - downX
                    val dy = event.rawY - downY
                    if (abs(dx) < shake && abs(dy) < shake) {
                        v.performClick()
                        return true
                    }
                    val param = binding.root.layoutParams as WindowManager.LayoutParams
                    savePosition(Pair(param.x, param.y))
                }
            }
            return true
        }

    }

    private fun getWindowLayoutParams(): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.format = PixelFormat.RGBA_8888
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
//                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
//                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        getSavedPosition().apply {
            layoutParams.x = first
            layoutParams.y = second
        }
        return layoutParams
    }

    private fun getSavedPosition(): Pair<Int, Int> {
        val x = preferences.getInt("windows_position_x", 0)
        val y = preferences.getInt("windows_position_y", 0)
        val position = Pair(x, y)
        Timber.d("get saved position: $position")
        return position
    }

    private fun savePosition(position: Pair<Int, Int>) {
        Timber.d("save position: $position")
        preferences.edit()
            .putInt("windows_position_x", position.first)
            .putInt("windows_position_y", position.second)
            .apply()
    }
}