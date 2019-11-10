package com.example.vittles.wastereport

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.vittles.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import kotlinx.android.synthetic.main.fragment_bar_chart.*

class FragmentBarChart : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_bar_chart, container, false)

        setupBarChartData(view)
        setupBarChartData2(view)
        return view

    }

    private fun setupBarChartData(view:View) {

        val barChart = view.findViewById<BarChart>(R.id.barChart)
        // create BarEntry for Bar Group
        val bargroup = ArrayList<BarEntry>()
        bargroup.add(BarEntry(0f, 70f, "0"))
        bargroup.add(BarEntry(1f, 85f, "1"))
        bargroup.add(BarEntry(2f, 90f, "2"))
        bargroup.add(BarEntry(3f, 83f, "3"))
        bargroup.add(BarEntry(4f, 78f, "4"))
        bargroup.add(BarEntry(5f, 90f, "5"))
        bargroup.add(BarEntry(6f, 96f, "6"))

        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "")
        barDataSet.color = Color.rgb(38, 243, 145)

        val barChartRender =
            CustomBarChartRender(barChart, barChart.animator, barChart.viewPortHandler)
        barChartRender.setRadius(30)
        barChart.renderer = barChartRender

        val data = BarData(barDataSet)
        barChart.setData(data)

        designSetup(barChart)
        barChart.xAxis.isEnabled = false
        barChart.setViewPortOffsets(80f, 40f, 80f, 10f)

    }

    private fun setupBarChartData2(view: View) {

        val barChart2 = view.findViewById<BarChart>(R.id.barChart2)

        val labels = ArrayList<String>()
        labels.add("Mon")
        labels.add("Tue")
        labels.add("Wed")
        labels.add("Thu")
        labels.add("Fri")
        labels.add("Sat")
        labels.add("Sun")

        // create BarEntry for Bar Group
        val bargroup = ArrayList<BarEntry>()
        bargroup.add(BarEntry(0f, 14f, "2"))
        bargroup.add(BarEntry(1f, 10f, "12"))
        bargroup.add(BarEntry(2f, 22f, "22"))
        bargroup.add(BarEntry(3f, 17f, "31"))
        bargroup.add(BarEntry(4f, 10f, "43"))
        bargroup.add(BarEntry(5f, 15f, "54"))
        bargroup.add(BarEntry(6f, 30f, "65"))


        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "")

        val barChartRender =
            CustomBarChartRender(barChart2, barChart2.animator, barChart2.viewPortHandler)
        barChartRender.setRadius(25)
        barChart2.renderer = barChartRender

        barDataSet.color = Color.RED

        val data = BarData(barDataSet)
        barChart2.setData(data)
        designSetup(barChart2)
        barChart2.xAxis.textSize = 12f
        barChart2.xAxis.textColor = Color.BLACK
        barChart2.xAxis.labelRotationAngle = 180f
        barChart2.xAxis.position = XAxis.XAxisPosition.TOP

        barChart2.rotation = 180f
        barChart2.setViewPortOffsets(80f,100f,80f,10f)
        barChart2.xAxis.xOffset = 20f
        barChart2.xAxis.yOffset = 20f
        barChart2.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart2.xAxis.setGranularity(1f)
        barChart2.xAxis.setGranularityEnabled(true)

    }

    fun designSetup(barChart: BarChart) {
        barChart.setPinchZoom(false)
        barChart.setScaleEnabled(false)
        barChart.isHighlightPerTapEnabled = false
        barChart.isHighlightPerDragEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        //barChart.axisRight.setDrawGridLines(false)
        //barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)
        barChart.axisLeft.setDrawAxisLine(false)
        barChart.axisRight.setDrawAxisLine(false)
        barChart.clipToPadding = true
        barChart.axisLeft.spaceBottom = 0f
        barChart.axisRight.spaceBottom = 0f
        barChart.xAxis.xOffset = 0f
        barChart.xAxis.yOffset = 0f
        barChart.data.barWidth = 0.4f
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.legend.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.data.setDrawValues(false)
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisRight.axisMaximum = 100f
        barChart.axisRight.axisMinimum = 0f
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.granularity = 50f
        barChart.axisRight.granularity = 50f
    }

}