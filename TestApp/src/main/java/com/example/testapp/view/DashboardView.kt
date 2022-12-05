package com.example.testapp.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.core.content.res.getResourceIdOrThrow
import com.example.testapp.R
import java.lang.Math.PI
import java.lang.StrictMath.sin
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.max

/**
 * @author Virogu
 * @since 2022-12-02 15:28
 **/
class DashboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {

    interface Callback {

        fun getProgressText(value: Float): String = BigDecimal(value.toString())
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()

        fun getScaleText(value: Float): String = BigDecimal(value.toString())
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()

        fun getDescText(value: Float): String = ""
    }

    companion object {
        private const val debug = false
        private const val defaultValueSize = 80f
        private const val defaultScaleTextSize = 14f
        private const val defaultScaleTextMargin = 0f
        private const val defaultDescTextSize = 20f
        private const val defaultArcWidth = 20f
        private const val defaultDescMargin = 8f
        private const val defaultIconMargin = 8f
    }

    private var callback: Callback? = object : Callback {

    }

    private var arcInnerColor: Int = Color.parseColor("#355EDC")
    private var arcOutColor: Int = Color.parseColor("#E6E6E8")

    private var arcLineWidth: Float = defaultArcWidth.dp

    private var valueTextSize: Float = defaultValueSize.sp
    private var scaleTextSize: Float = defaultScaleTextSize.sp
    private var descTextSize: Float = defaultDescTextSize.sp

    private var descTextMargin: Float = defaultDescMargin.dp
    private var scaleTextMargin: Float = defaultScaleTextMargin.dp
    private var iconMargin: Float = defaultIconMargin.dp

    private var cutAngle: Float = 45f
        set(value) {
            field = value
            startAngle = 90 + value
            sweepAngle = 360 - (value * 2)
        }
    private var startAngle: Float = 90 + cutAngle
    private var sweepAngle: Float = 360 - (cutAngle * 2)

    private var arcValueColor: Int = Color.parseColor("#355EDC")
    private var arcScaleColor: Int = Color.parseColor("#999999")
    private var arcDescColor: Int = Color.parseColor("#999999")

    private var maxProgress: Float = 120f
    private var minProgress: Float = 20f
    private var currentProgress: Float = 80f
        set(value) {
            field = value
            processText = callback?.getProgressText(currentProgress) ?: ""
            descText = callback?.getDescText(currentProgress) ?: ""
        }

    private var progressStep: Float = 10f

    private var processText: String = callback?.getProgressText(currentProgress) ?: ""
    private var descText: String = callback?.getDescText(currentProgress) ?: ""

    private var bitmap: Bitmap? = null

    private val paint = Paint()
    private val textPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        alpha = 255
        textAlign = Paint.Align.CENTER
    }
    private val rectF = RectF()
    private val rect = Rect()

    private var offsetH: Float = 0f
    private var centerX: Float = width / 2f
    private var centerY: Float = height / 2f

    private var size = max(width, height)

    private var scaleRadius: Float = width.toFloat()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val size = w.coerceAtMost(h)
        val r = size / 2
        val minusH = r - r * cos((startAngle - 90).degree)
        val actualH = size - minusH
        offsetH = (size - actualH).toFloat()
        setMeasuredDimension(size, actualH.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = max(w, h)
        centerX = w / 2f
        centerY = h / 2f
        scaleRadius = (size / 2f) - arcLineWidth * 2 - scaleTextMargin
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //外环
        paint.apply {
            reset()
            //抗锯齿
            isAntiAlias = true
            //防抖动
            isDither = true
            //宽度
            strokeWidth = arcLineWidth
            //颜色
            color = arcOutColor
            //设置状态
            style = Paint.Style.STROKE
            //头部小圆点
            strokeCap = Paint.Cap.ROUND
        }
        val tempW = arcLineWidth / 2
        rectF.set(
            tempW,
            tempW,
            size - tempW,
            size - tempW
        )
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)

        //内环
        paint.color = arcInnerColor
        canvas.drawArc(
            rectF,
            startAngle,
            ((currentProgress - minProgress) / (maxProgress - minProgress)) * sweepAngle,
            false,
            paint
        )

        if (debug) {
            paint.apply {
                reset()
                textSize = 10f
                color = Color.RED
                style = Paint.Style.STROKE
                strokeWidth = 3f
            }
            canvas.drawLine(0F, centerY, width.toFloat(), centerY, paint)
            canvas.drawLine(centerX, 0f, centerX, height.toFloat(), paint)
        }
        //画文字
        textPaint.textSize = valueTextSize
        val valueMetrics = textPaint.fontMetricsInt
        val valueH = valueMetrics.descent - valueMetrics.ascent

        textPaint.textSize = descTextSize
        val descMetrics = textPaint.fontMetricsInt
        val descH = if (descText.isEmpty()) 0 else descMetrics.descent - descMetrics.ascent
        val margin = if (descText.isEmpty()) 0f else descTextMargin

        val halfH = (valueH + descH + margin) / 2
        val y1 = halfH - descH - margin - valueMetrics.descent

        textPaint.apply {
            color = arcValueColor
            textSize = valueTextSize
        }
        canvas.drawText(processText, centerX, centerY + y1, textPaint)

        if (descText.isNotEmpty()) {
            textPaint.apply {
                color = arcDescColor
                textSize = descTextSize
            }
            val y2 = halfH - descMetrics.descent
            // canvas.drawLine(drawX, baselineDesc, drawX + rect.width(), baselineDesc, paint)
            canvas.drawText(descText, centerX, centerY + y2, textPaint)
        }

        bitmap?.also { bp ->
            paint.reset()
            canvas.drawBitmap(bp, centerX - bp.width / 2, height - bp.height - iconMargin, paint)
        }

        textPaint.apply {
            color = arcDescColor
            textSize = scaleTextSize
        }
        // 每项的间距角度
        if (debug) {
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(centerX, centerY + offsetH / 2, scaleRadius, paint)
        }
        //val deltaAngle = (sweepAngle / (meters.size - 1))
        val scaleCount = (maxProgress - minProgress) / progressStep
        val deltaAngle = sweepAngle / scaleCount
        repeat(scaleCount.toInt() + 1) { index ->
            val s = callback?.getScaleText(minProgress + index * progressStep)?.takeIf {
                it.isNotEmpty()
            } ?: return@repeat
            val currentAngleInRad = (deltaAngle * index + startAngle)
            val offsetX = (cos(currentAngleInRad.degree) * (scaleRadius)).toInt()
            val offsetY = (sin(currentAngleInRad.degree) * (scaleRadius)).toInt()
            val x = centerX + offsetX
            val y = centerY + offsetY + offsetH / 2
            if (debug) {
                canvas.drawLine(centerX, centerY + offsetH / 2, x, y, paint)
            }
            canvas.drawText(s, x, y, textPaint)
        }
    }

    private val Float.degree
        get() = this * PI / 180f

    val Float.radian
        get() = this * 180f / PI

    private val Int.degree
        get() = this * PI / 180f

    private val Int.radian
        get() = this * 180f / PI

    private val Double.degree
        get() = this * PI / 180f

    private val Double.radian
        get() = this * 180f / PI

    private val Float.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

    private val Float.px
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            this,
            Resources.getSystem().displayMetrics
        )

    private val Float.sp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )

    private val Int.dp
        get() = toFloat().dp

    private val Int.sp
        get() = toFloat().sp

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DashboardView).use {
            arcInnerColor = it.getColor(R.styleable.DashboardView_arcInnerColor, arcInnerColor)
            arcOutColor = it.getColor(R.styleable.DashboardView_arcOutColor, arcOutColor)
            arcLineWidth = it.getDimension(R.styleable.DashboardView_arcWidth, defaultArcWidth).dp

            valueTextSize =
                it.getDimension(R.styleable.DashboardView_arcValueTextSize, defaultValueSize).dp
            arcValueColor = it.getColor(R.styleable.DashboardView_arcTextColor, arcValueColor)

            scaleTextSize =
                it.getDimension(R.styleable.DashboardView_arcScaleColor, defaultScaleTextSize).dp
            scaleTextMargin =
                it.getDimension(R.styleable.DashboardView_arcScaleMargin, defaultScaleTextMargin).dp
            arcScaleColor = it.getColor(R.styleable.DashboardView_arcScaleColor, arcScaleColor)

            descTextSize =
                it.getDimension(R.styleable.DashboardView_arcDescTextSize, defaultDescTextSize).dp
            arcDescColor = it.getColor(R.styleable.DashboardView_arcDescColor, arcDescColor)
            descTextMargin =
                it.getDimension(R.styleable.DashboardView_arcDescMargin, defaultDescMargin).dp

            bitmap = runCatching {
                BitmapFactory.decodeResource(
                    resources,
                    it.getResourceIdOrThrow(R.styleable.DashboardView_arcIcon)
                )
            }.getOrNull()

            iconMargin =
                it.getDimension(R.styleable.DashboardView_arcIconMargin, defaultIconMargin).dp

            cutAngle = it.getFloat(R.styleable.DashboardView_arcCutAngle, cutAngle)

            maxProgress = it.getFloat(R.styleable.DashboardView_arcMaxProgress, maxProgress)

            minProgress = it.getFloat(R.styleable.DashboardView_arcMinProgress, minProgress)
                .coerceAtMost(maxProgress)

            currentProgress = it.getFloat(R.styleable.DashboardView_arcProgress, currentProgress)
                .coerceAtLeast(minProgress)
                .coerceAtMost(maxProgress)
        }
    }

    /**
     * 外弧颜色
     */
    fun setOuterArchColor(@ColorInt color: Int) {
        arcOutColor = color
        postInvalidate()
    }

    /**
     * 内弧颜色
     */
    fun setInnerArchColor(@ColorInt color: Int) {
        arcInnerColor = color
        postInvalidate()
    }

    /**
     * 圆弧宽度
     */
    fun setArchWidth(dp: Int) {
        arcLineWidth = dp.dp
        postInvalidate()
    }

    /**
     * 进度数值文字大小
     */
    fun setProgressTextSize(sp: Int) {
        valueTextSize = sp.sp
        postInvalidate()
    }

    /**
     * 进度数值文字颜色
     */
    fun setProgressTextColor(@ColorInt color: Int) {
        arcValueColor = color
        postInvalidate()
    }

    /**
     * 刻度值文字大小
     */
    fun setScaleTextSize(sp: Int) {
        scaleTextSize = sp.toFloat().dp
        postInvalidate()
    }

    /**
     * 刻度值文字颜色
     */
    fun setScaleTextColor(@ColorInt color: Int) {
        arcScaleColor = color
        postInvalidate()
    }

    /**
     * 刻度值和圆弧间距
     */
    fun setScaleTextMargin(dp: Int) {
        scaleTextMargin = dp.dp
        postInvalidate()
    }

    /**
     * 描述文字大小
     */
    fun setDescTextSize(sp: Int) {
        descTextSize = sp.toFloat().dp
        postInvalidate()
    }

    /**
     * 描述文字颜色
     */
    fun setDescTextColor(@ColorInt color: Int) {
        arcDescColor = color
        postInvalidate()
    }

    /**
     * 描述文字和数值间距
     */
    fun setDescTextMargin(dp: Int) {
        descTextMargin = dp.dp
        postInvalidate()
    }

    /**
     * 底部图标
     */
    fun setIconResource(@DrawableRes iconId: Int) {
        bitmap = runCatching {
            BitmapFactory.decodeResource(resources, iconId)
        }.getOrNull()
        postInvalidate()
    }

    /**
     * 底部图标间距
     */
    fun setIconMargin(dp: Int) {
        iconMargin = dp.dp
        postInvalidate()
    }

    /**
     * 当前进度数值
     */
    fun setProgress(value: Float) {
        currentProgress = value.coerceAtLeast(minProgress).coerceAtMost(maxProgress)
        postInvalidate()
    }

    /**
     * 最小进度
     */
    fun setMinProgress(min: Float) {
        minProgress = min.coerceAtMost(maxProgress)
        postInvalidate()
    }

    /**
     * 最大进度
     */
    fun setMaxProgress(max: Float) {
        maxProgress = max
        postInvalidate()
    }

    /**
     * 刻度阶梯，每隔多少画一个刻度
     */
    fun setStep(step: Float) {
        if (step > maxProgress || step <= 0) {
            return
        }
        progressStep = step
        postInvalidate()
    }

    /**
     * 圆弧切除的角度
     */
    fun cutAngle(
        @FloatRange(from = 0.0, to = 180.0)
        cutAngle: Float
    ) {
        this.cutAngle = cutAngle
        postInvalidate()
    }

    /**
     * 获取刻度和描述文字的callback
     */
    fun setCallback(callback: Callback?) {
        this.callback = callback
        postInvalidate()
    }

}