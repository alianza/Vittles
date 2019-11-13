package com.example.vittles.wastereport.BarChart

import com.github.mikephil.charting.charts.BarChart
import org.joda.time.DateTime


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