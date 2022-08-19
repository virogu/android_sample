package com.virogu.motionlayout

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.virogu.motionlayout.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = "TransitionListener"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.init()
    }

    private fun ActivityMainBinding.init() {
        val d = WaveBitmapDrawable(resources).apply {
            setBackgroundColor(Color.parseColor("#0277BD"))
            //setWaveColor(Color.parseColor("#0277BD"))
            setCornerRadius(0)
            setReverse(true)
            setWaveAlpha(100)
        }
        mView.setImageDrawable(d)
        layRoot.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.i(TAG, "onTransitionStarted: p1: $p1, p2: $p2")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Log.i(TAG, "onTransitionChange: p1: $p1, p2: $p2, p3: $p3")
                d.progress = p3
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.i(TAG, "onTransitionCompleted: p1: $p1")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.i(TAG, "onTransitionTrigger: p1: $p1, p2: $p2, p3: $p3")
            }

        })
        lifecycleScope.launch(Dispatchers.Main) {
            d.start()
            for (i in 0..100) {
                delay(100)
                //if (i > 50) {
                //    rootView.transitionToStart()
                //    break
                //}
                layRoot.progress = i.toFloat() / 100.0f
            }

        }
    }
}