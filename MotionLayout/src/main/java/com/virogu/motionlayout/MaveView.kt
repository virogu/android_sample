package com.virogu.motionlayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable


open class MaveView : View {
    private var mAbovePath: Path? = null
    private var mBelowWavePath: Path? = null
    private var mAboveWavePaint: Paint? = null
    private var mBelowWavePaint: Paint? = null
    private var mDrawFilter: DrawFilter? = null
    private var a = 0f
    private var b = 0.0
    private var y1 = 0f
    private val y2 = 0f
    private var mWaveAnimationListener: OnWaveAnimationListener? = null

    constructor(context: Context?) : super(context) {
        //初始化
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        //初始化
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        //初始化
        init()
    }

    protected fun init() {

        //初始化路径
        mAbovePath = Path()
        mBelowWavePath = Path()
        //初始化画笔
        //上方波浪
        mAboveWavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mAboveWavePaint!!.isAntiAlias = true
        mAboveWavePaint!!.style = Paint.Style.FILL
        mAboveWavePaint!!.color = Color.parseColor("#0277BD")
        //下方波浪
        mBelowWavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBelowWavePaint!!.isAntiAlias = true
        mBelowWavePaint!!.style = Paint.Style.FILL
        mBelowWavePaint!!.color = Color.BLUE
        mBelowWavePaint!!.alpha = 60
        //画布抗锯齿
        mDrawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawFilter = mDrawFilter
        mAbovePath!!.reset()
        mBelowWavePath!!.reset()
        a -= 0.1f
        b = 2 * Math.PI / width
        mAbovePath!!.moveTo(left.toFloat(), bottom - 15.toFloat())
        //        mBelowWavePath.moveTo(getLeft(), getBottom() + 15);
        for (x in 0..width) {
            /**
             * y=Asin(ωx+φ)+k
             * A—振幅越大，波形在y轴上最大与最小值的差值越大
             * ω—角速度， 控制正弦周期(单位角度内震动的次数)
             * φ—初相，反映在坐标系上则为图像的左右移动。这里通过不断改变φ,达到波浪移动效果
             * k—偏距，反映在坐标系上则为图像的上移或下移。
             */
            y1 = (30 * Math.cos(b * x + a) + 30).toFloat()
            //            y2 = (float) (30 * Math.sin(ω * x + φ) + 30);
            mAbovePath!!.lineTo(x.toFloat(), y1)
            //            mBelowWavePath.lineTo(x, y2);
            if (x == width / 2) mWaveAnimationListener!!.OnWaveAnimation(y1)
        }
        //回调 把y坐标的值传出去(在activity里面接收让小机器人随波浪一起摇摆)
        mAbovePath!!.lineTo(right.toFloat(), bottom - 15.toFloat())
        //        mBelowWavePath.lineTo(getRight(), getBottom() + 15);
        canvas.drawPath(mAbovePath!!, mAboveWavePaint!!)
        //        canvas.drawPath(mBelowWavePath, mBelowWavePaint);
        postInvalidateDelayed(20)
    }

    fun setOnWaveAnimationListener(onWaveAnimationListener: OnWaveAnimationListener?) {
        mWaveAnimationListener = onWaveAnimationListener
    }

    interface OnWaveAnimationListener {
        fun OnWaveAnimation(y: Float)
    }
}