package com.example.vittles.wastereport.barchart

import java.text.DecimalFormat
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter


class MyValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("###")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return format.format(100 - value.toInt())
    }
}