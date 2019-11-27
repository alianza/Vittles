package com.example.vittles.wastereport.barchart

import com.example.domain.wasteReport.BarChartEntry
import com.github.mikephil.charting.charts.BarChart
import org.joda.time.DateTime


/**
 * MVP Contract for bar chart.
 *
 * @author Sarah Lange
 */
interface BarChartContract {

    interface View {
        fun setupBarChartDataEaten(barChartEntries: List<BarChartEntry>)
        fun setupBarChartDataExpired(barChartEntries: List<BarChartEntry>)
        fun setupDesign(barChart: BarChart)
        fun setupCharts(barChartData: List<BarChartEntry>)
        fun setXAxisLabels(barChartEntries: List<BarChartEntry>, barChart: BarChart)
        fun setRadius(barChart: BarChart)
        fun fail()

    }

    interface Presenter {
        fun getWasteReportProducts(date: DateTime)

    }
}