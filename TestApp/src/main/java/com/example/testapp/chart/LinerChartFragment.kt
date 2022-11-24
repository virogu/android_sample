package com.example.testapp.chart

import android.graphics.Color
import android.graphics.DashPathEffect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.testapp.databinding.FragmentLinerChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.Utils
import com.virogu.base.fragment.BaseBindingFragment
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random


sealed class SignsData(
    val name: String,
    val unit: String,
    open var xValue: Float,
    open val time: String,
    open var value: Float,
) : Entry(xValue, value) {

    override fun getX(): Float {
        return xValue
    }

    override fun setX(x: Float) {
        xValue = x
    }

    override fun getY(): Float {
        return value
    }

    override fun setY(y: Float) {
        value = y
    }

    data class TemperatureSignsData(
        override var xValue: Float,
        override val time: String,
        override var value: Float,
    ) : SignsData("体温", "℃", xValue, time, value)

    data class PulseSignsData(
        override var xValue: Float,
        override val time: String,
        override var value: Float,
    ) : SignsData("脉搏", "次/分", xValue, time, value)
}

class LinerChartFragment : BaseBindingFragment<FragmentLinerChartBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLinerChartBinding {
        return FragmentLinerChartBinding.inflate(inflater, container, false)
    }

    override fun FragmentLinerChartBinding.onViewCreated() {
        lineChart.customConfig().apply {
            data = getRandomData()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(5000)
            lineChart.data = getRandomData()
            lineChart.invalidate()
        }
    }

    private fun getRandomData(): LineData {
        // 定义LineDataSet
        val temperatureSetList = ArrayList<Entry>().apply {
            (1..12).forEach {
                SignsData.TemperatureSignsData(
                    it.toFloat(),
                    "2022-11-23 ${2 * it}:00",
                    (((Random.nextFloat() + Random.nextInt(
                        35,
                        38
                    )) * 10).roundToInt() / 10.0f)
                ).also(::add)
            }
        }
        val temperatureSet = LineDataSet(temperatureSetList, "体温（℃） ").apply {
            setColors(Color.RED)
            setCircleColor(Color.RED)
            //isHighlightEnabled = false
            setDrawHighlightIndicators(false)
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawValues(false)
        }

        val pulseSetList = ArrayList<Entry>().apply {
            (1..12).forEach {
                SignsData.PulseSignsData(
                    it.toFloat(),
                    "2022-11-23 ${2 * it}:00",
                    ((Random.nextFloat() + Random.nextInt(60, 150)).roundToInt()).toFloat()
                ).apply {
                    x = it.toFloat()
                    y = value
                }.also(::add)
            }
        }

        val pulseSet = LineDataSet(pulseSetList, "脉搏（次/分） ").apply {
            //isHighlightEnabled = false
            setDrawHighlightIndicators(false)
            setColors(Color.BLUE)
            setCircleColor(Color.BLUE)
            axisDependency = YAxis.AxisDependency.LEFT
            setDrawValues(false)
        }
        // 定义LineData 将lineDataSet传入
        return LineData(pulseSet, temperatureSet)
    }

    private fun LineChart.customConfig(): LineChart {
        setDrawGridBackground(false)
        setDrawBorders(false)
        isDragEnabled = false
        isScaleXEnabled = false
        isScaleYEnabled = false
        setPinchZoom(false)
        xAxis.also {
            it.position = XAxis.XAxisPosition.BOTTOM
            // 设置X轴上显示的值
            //val valueFormatter = (0..24).toList().map { "$it" }
            //it.valueFormatter = IndexAxisValueFormatter(valueFormatter)
            it.valueFormatter = IndexAxisValueFormatter(emptyList())
            // 设置粒度 1的话就是默认X轴的值都显示
            it.granularity = 3.0f
            it.axisMinimum = 0f
            it.axisMaximum = 13f
            it.axisLineWidth = 1f
            it.setGridDashedLine(DashPathEffect(floatArrayOf(5f, 3f), 0f))
            //图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
            it.setAvoidFirstLastClipping(true)
        }
        axisLeft.also {
            it.axisMaximum = 180f
            it.axisMinimum = 20f
            it.textColor = Color.BLUE
            it.textSize = Utils.convertDpToPixel(24f)
            it.setLabelCount(((it.axisMaximum - it.axisMinimum) / 20).toInt() + 1, true)
            it.drawBottomLabelEntry(false)
            it.setGridDashedLine(DashPathEffect(floatArrayOf(5f, 3f), 0f))
        }
        axisRight.also {
            it.axisMaximum = 42f
            it.axisMinimum = 34f
            it.drawBottomLabelEntry(false)
            it.setLabelCount(((it.axisMaximum - it.axisMinimum) / 1).toInt() + 1, true)
            it.textColor = Color.RED
            it.textSize = Utils.convertDpToPixel(24f)
            it.setGridDashedLine(DashPathEffect(floatArrayOf(5f, 3f), 0f))
        }
        // 详细文字介绍
        description.isEnabled = false
        legend.also {
            it.form = Legend.LegendForm.CIRCLE
            it.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            it.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            it.textSize = Utils.convertDpToPixel(20f)
            it.formSize = 18f
            it.yOffset = 10f
        }
        val mv = MyMarkerView(requireContext())
        mv.chartView = this
        marker = mv
        // animateX(2000) { it * 2 }
        return this
    }

    private fun YAxis.drawBottomLabelEntry(enable: Boolean) = runCatching {
        val field = YAxis::class.java.getDeclaredField("mDrawBottomYLabelEntry")
        field.isAccessible = true
        field.setBoolean(this, enable)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LinerChartFragment()
    }

}