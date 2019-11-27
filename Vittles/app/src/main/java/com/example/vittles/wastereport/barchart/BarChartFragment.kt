package com.example.vittles.wastereport.barchart


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.domain.wasteReport.BarChartEntry
import com.example.vittles.R
import com.example.vittles.wastereport.circlechart.RefreshData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.android.support.DaggerFragment
import org.joda.time.DateTime
import org.joda.time.Days
import javax.inject.Inject

class BarChartFragment @Inject internal constructor(var date: DateTime) : DaggerFragment(), BarChartContract.View, RefreshData  {
    @Inject
    lateinit var presenter: BarChartPresenter

    lateinit var barChartEaten: BarChart
    lateinit var barChartExpired: BarChart
    var days = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        days = Days.daysBetween(date.withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay()).days
        barChartEaten = view.findViewById(R.id.barChart)
        barChartExpired = view.findViewById(R.id.barChartExpired)
        presenter.start(this)
        presenter.getWasteReportProducts(date)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun refresh(date: DateTime, vittlesEaten: Int, vittlesExpired: Int) {
        this.date = date
        //presenter.getEatenPercent(date)
    }

    override fun setupCharts(barChartData: List<BarChartEntry>){
        setupBarChartDataEaten(barChartData)
        setupBarChartDataExpired(barChartData)
    }
    override fun setupBarChartDataEaten(list: List<BarChartEntry>) {
        val barGroup = ArrayList<BarEntry>()

        for(i in 0..6){
            barGroup.add(BarEntry(i.toFloat(), list[6 - i].vittlesEatenPercent, i.toString()))
        }
        val barDataSet = BarDataSet(barGroup, "")
        barDataSet.color = Color.rgb(38, 243, 145)

        val barChartRender =
            CustomBarChartRender(
                barChartEaten,
                barChartEaten.animator,
                barChartEaten.viewPortHandler
            )
        barChartRender.setRadius(30)

        val barData = BarData(barDataSet)

        barChartEaten.apply {
            renderer = barChartRender
            data = barData
            xAxis.isEnabled = false
            setViewPortOffsets(80f, 40f, 80f, 10f)
        }

        designSetup(barChartEaten)




    }

    override fun setupBarChartDataExpired(list: List<BarChartEntry>) {
        val labels = ArrayList<String>()
        val barGroup = ArrayList<BarEntry>()

        for(i in 0..6){
            barGroup.add(BarEntry(i.toFloat(), list[i].vittlesExpiredPercent, i.toString()))
            labels.add(list[i].getWeekday())
        }
        val barDataSet = BarDataSet(barGroup, "")

        val barChartRender =
            CustomBarChartRender(
                barChartExpired,
                barChartExpired.animator,
                barChartExpired.viewPortHandler
            )
        barChartRender.setRadius(25)
        barChartExpired.renderer = barChartRender

        barChartExpired.rendererLeftYAxis = CustomYAxisRenderer(barChartExpired.viewPortHandler, barChartExpired.axisLeft, barChartExpired.rendererXAxis.transformer, barChartExpired.xAxis)

        barDataSet.color = Color.RED

        val data = BarData(barDataSet)
        barChartExpired.data = data
        designSetup(barChartExpired)

        barChartExpired.xAxis.apply {
            textSize = 12f
            textColor = Color.BLACK
            labelRotationAngle = 180f
            position = XAxis.XAxisPosition.TOP
            xOffset = 20f
            yOffset = 20f
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            isGranularityEnabled = true
        }

        barChartExpired.rotation = 180f
        barChartExpired.setViewPortOffsets(80f,100f,80f,25f)

        barChartExpired.axisRight.valueFormatter =  MyValueFormatter()
        barChartExpired.axisLeft.valueFormatter =  MyValueFormatter()

    }

    override fun fail() {
    }


    override fun designSetup(barChart: BarChart) {
        barChart.setPinchZoom(false)
        barChart.setScaleEnabled(false)
        barChart.isHighlightPerTapEnabled = false
        barChart.isHighlightPerDragEnabled = false
        barChart.clipToPadding = true
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.legend.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.data.apply {
            barWidth = 0.4f
            setDrawValues(false)
        }
        barChart.xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            xOffset = 0f
            yOffset = 0f

        }

        barChart.axisLeft.apply {
            setDrawAxisLine(false)
            axisMinimum = 0f
            spaceBottom = 0f
            axisMaximum = 100f
            granularity = 50f
        }
        barChart.axisRight.apply{
            setDrawAxisLine(false)
            spaceBottom = 0f
            axisMaximum = 100f
            axisMinimum = 0f
            granularity = 50f
        }
    }

}