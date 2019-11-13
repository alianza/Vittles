package com.example.vittles.wastereport.BarChart


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vittles.R
import com.example.vittles.wastereport.CircleChart.RefreshData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.android.support.DaggerFragment
import org.joda.time.DateTime
import javax.inject.Inject

class BarChartFragment @Inject internal constructor(var date: DateTime) : DaggerFragment(), BarChartContract.View, RefreshData  {
    @Inject
    lateinit var presenter: BarChartPresenter

    lateinit var barChartEaten: BarChart
    lateinit var barChartExpired: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barChartEaten = view.findViewById(R.id.barChart)
        barChartExpired = view.findViewById(R.id.barChartExpired)
        setupBarChartDataEaten()
        setupBarChartDataExpired()
    }

    override fun refresh(date: DateTime) {
        this.date = date
        //presenter.getEatenPercent(date)
    }

    override fun setupBarChartDataEaten() {

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
            CustomBarChartRender(
                barChartEaten,
                barChartEaten.animator,
                barChartEaten.viewPortHandler
            )
        barChartRender.setRadius(30)
        barChartEaten.renderer = barChartRender

        val data = BarData(barDataSet)
        barChartEaten.setData(data)

        designSetup(barChartEaten)
        barChartEaten.xAxis.isEnabled = false
        barChartEaten.setViewPortOffsets(80f, 40f, 80f, 10f)

    }

    override fun setupBarChartDataExpired() {
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
            CustomBarChartRender(
                barChartExpired,
                barChartExpired.animator,
                barChartExpired.viewPortHandler
            )
        barChartRender.setRadius(25)
        barChartExpired.renderer = barChartRender

        barDataSet.color = Color.RED

        val data = BarData(barDataSet)
        barChartExpired.setData(data)
        designSetup(barChartExpired)
        barChartExpired.xAxis.textSize = 12f
        barChartExpired.xAxis.textColor = Color.BLACK
        barChartExpired.xAxis.labelRotationAngle = 180f
        barChartExpired.xAxis.position = XAxis.XAxisPosition.TOP

        barChartExpired.rotation = 180f
        barChartExpired.setViewPortOffsets(80f,100f,80f,10f)
        barChartExpired.xAxis.xOffset = 20f
        barChartExpired.xAxis.yOffset = 20f
        barChartExpired.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChartExpired.xAxis.setGranularity(1f)
        barChartExpired.xAxis.setGranularityEnabled(true)

    }

    override fun designSetup(barChart: BarChart) {
        barChart.setPinchZoom(false)
        barChart.setScaleEnabled(false)
        barChart.isHighlightPerTapEnabled = false
        barChart.isHighlightPerDragEnabled = false
        barChart.xAxis.setDrawGridLines(false)
        //barChartEaten.axisRight.setDrawGridLines(false)
        //barChartEaten.axisLeft.setDrawGridLines(false)
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