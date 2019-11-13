package com.example.vittles.wastereport.CircleChart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vittles.R
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import dagger.android.support.DaggerFragment
import org.joda.time.DateTime
import javax.inject.Inject

interface RefreshData {
    fun refresh(date: DateTime)
}

class CircleChartFragment @Inject internal constructor(var date: DateTime, var vittlesEaten: Int, var vittlesExpired: Int) : DaggerFragment(), CircleChartContract.View, RefreshData  {
    @Inject
    lateinit var presenter: CircleChartPresenter

    lateinit var dynamicArgView: DecoView
    lateinit var textPercentage: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_circle_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dynamicArgView = view.findViewById(R.id.dynamicArcView)
        textPercentage = view.findViewById(R.id.textPercentage)
        presenter.start(this)
        presenter.getEatenPercent(date, vittlesEaten, vittlesExpired)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun refresh(date: DateTime) {
        this.date = date
        presenter.getEatenPercent(date, vittlesEaten, vittlesExpired)
    }

    override fun drawCircleChart(percent: Int) {

        val seriesItem = SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.lightGrey))
            .setRange(0f, 100f, 0f)
            .build()

        val seriesItem2 = SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimary))
            .setRange(0f, 100f, 0f)
            .build()

        val backIndex = dynamicArgView.addSeries(seriesItem)



        seriesItem2.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
            override fun onSeriesItemAnimationProgress(
                percentComplete: Float,
                currentPosition: Float
            ) {
                val percentFilled =
                    (currentPosition - seriesItem2.minValue) / (seriesItem2.maxValue - seriesItem2.minValue)
                textPercentage.text = String.format("%.0f%%", percentFilled * 100f)
            }

            override fun onSeriesItemDisplayProgress(percentComplete: Float) {

            }
        })

        dynamicArgView.addEvent(
            DecoEvent.Builder(100f)
                .setIndex(backIndex)
                .setDuration(1)
                .build()
        )

        if(percent.toFloat() == 0f) {
            textPercentage.text = "0%"
        } else {
            val series1Index = dynamicArgView.addSeries(seriesItem2)
            dynamicArgView.addEvent(
                DecoEvent.Builder(percent.toFloat())
                    .setIndex(series1Index)
                    .build()
            )
        }

    }

    override fun setNoResultsView() {
        Toast.makeText(context, "Error Cicrle Chart", Toast.LENGTH_SHORT).show()

    }

}
