package com.example.vittles.wastereport.barchart

import com.github.mikephil.charting.charts.BarChart


/**
 * MVP Contract for circle chart.
 *
 * @author Sarah Lange
 */
interface BarChartContract {

    interface View {
        fun setupBarChartDataEaten()
        fun setupBarChartDataExpired()
        fun designSetup(barChart: BarChart)

    }

    interface Presenter {

    }
}