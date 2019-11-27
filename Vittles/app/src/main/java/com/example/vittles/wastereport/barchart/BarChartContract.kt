package com.example.vittles.wastereport.barchart

import com.example.domain.wasteReport.BarChartEntry
import com.github.mikephil.charting.charts.BarChart
import org.joda.time.DateTime


/**
 * MVP Contract for circle chart.
 *
 * @author Sarah Lange
 */
interface BarChartContract {

    interface View {
        fun setupBarChartDataEaten(list: List<BarChartEntry>)
        fun setupBarChartDataExpired(list: List<BarChartEntry>)
        fun designSetup(barChart: BarChart)
        fun setupCharts(barChartData: List<BarChartEntry>)
        fun fail()

    }

    interface Presenter {
        fun getWasteReportProducts(date: DateTime)

    }
}