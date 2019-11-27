package com.example.vittles.wastereport.barchart

import android.graphics.Canvas
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
import android.graphics.Paint.Align
import android.opengl.ETC1.getWidth
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.utils.Utils

/**
 * Custom Y Axis Renderer. Rotates y labels 180°.
 *
 * @author Sarah Lange
 *
 * @property xAxis X axis of the bar chart.
 * @constructor
 * TODO
 *
 * @param viewPortHandler View port handler of the bar chart.
 * @param yAxis Y axis of the bar chart whose labels are to be rotated.
 * @param trans Transformer of the bar chart.
 */


class CustomYAxisRenderer(viewPortHandler: ViewPortHandler, yAxis: YAxis, trans: Transformer, private val xAxis: XAxis ): YAxisRenderer(viewPortHandler, yAxis, trans) {

    override fun renderAxisLabels(c: Canvas) {
        c.translate(c.width.toFloat(),0f)
        c.rotate(180f)
        c.translate(0f,-c.height.toFloat() - xAxis.mLabelHeight - xAxis.yOffset + 15f)

        if (!mYAxis.isEnabled || !mYAxis.isDrawLabelsEnabled)
            return

        super.renderAxisLabels(c)
    }
}
