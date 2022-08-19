package com.virogu.cardviewpager

/**
 * Create By virogu
 * virogu@foxmail.com
 * 2020-09-30
 */
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2

/**
 * @param viewPager ViewPager2对象
 * @param scrollRotation 滑动时动画旋转角度
 * @param offsetBase 卡片层叠间距
 * @param centerPageScale 中心卡片缩放比率
 */
class CardPagerTransform(
    private val viewPager: ViewPager2,
    private val scrollRotation: Int = 45,
    private val offsetBase: Int = 20,
    private val centerPageScale: Float = 0.85f
) : ViewPager2.PageTransformer {

    private var offscreenPageLimit = 1

    init {
        if (viewPager.offscreenPageLimit > 0) {
            offscreenPageLimit = viewPager.offscreenPageLimit
        }
    }

    override fun transformPage(page: View, position: Float) {
        val pagerWidth = viewPager.width
        val horizontalOffsetBase: Float =
            (pagerWidth - pagerWidth * centerPageScale) / 2 / offscreenPageLimit + dp2px(
                page.context,
                offsetBase
            )
        if (position >= offscreenPageLimit || position <= -1) {
            page.visibility = View.GONE
        } else {
            page.visibility = View.VISIBLE
        }

        when {
            position < -1 -> {
                page.rotation = 0f
                page.alpha = 1f
            }
            position < 0 -> {
                //rotation 旋转角度
                page.rotation = position * scrollRotation
                page.alpha = 1f
            }
            else -> {
                page.rotation = 0f
                page.translationX = (horizontalOffsetBase - page.width) * position
                page.alpha = 1f
            }
        }
        val scaleFactor = (centerPageScale - position * 0.1f).coerceAtMost(centerPageScale)
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor
        //设置层级
        ViewCompat.setElevation(page, (offscreenPageLimit - position) * 2)

    }

    companion object {

        fun dp2px(context: Context, dp: Int): Int {
            // mdpi 1dp=1px
            // hdpi 1dp=1.5px
            // xhdpi 720*1280 1dp=2px
            // xxhdpi 1080*1920 1dp=3px
            // xxxhdpi 1440*2560 1dp=4px
            //获得资源
            val resources: Resources = context.resources
            // 1个dp或sp等于多少个像素点  算是密度 也可以算是dp与像素的比率
            val density: Float = resources.displayMetrics.scaledDensity
            // 6.5-->6
            // 6.5+0.5=7
            val px = density * dp + 0.5f
            return px.toInt()
        }
    }

}