package com.virogu.motionlayout

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    private val TAG = "TransitionListener"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        val d = WaveBitmapDrawable(resources).apply {
            setBackgroundColor(Color.parseColor("#0277BD"))
            //setWaveColor(Color.parseColor("#0277BD"))
            setCornerRadius(0)
            setReverse(true)
            setWaveAlpha(100)
        }
        mView.setImageDrawable(d)
        rootView.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                Log.i(TAG,"onTransitionStarted: p1: $p1, p2: $p2")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Log.i(TAG,"onTransitionChange: p1: $p1, p2: $p2, p3: $p3")
                d.progress = p3
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.i(TAG,"onTransitionCompleted: p1: $p1")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                Log.i(TAG,"onTransitionTrigger: p1: $p1, p2: $p2, p3: $p3")
            }

        })
        GlobalScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                d.start()
            }

            for (i in 0..100) {
                delay(100)
                //if (i > 50) {
                //    rootView.transitionToStart()
                //    break
                //}
                rootView.progress = i.toFloat() / 100.0f
            }

        }
    }
}