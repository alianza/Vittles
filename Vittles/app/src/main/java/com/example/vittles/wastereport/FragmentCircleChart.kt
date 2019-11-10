package com.example.vittles.wastereport

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.vittles.R
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import kotlin.coroutines.coroutineContext

interface RefreshData {
    fun refresh(percent: Int)
}


class FragmentCircleChart(var data: Int, context: Context) : Fragment(), RefreshData {
    private val applicationContext = context

    override fun refresh(percent: Int) {
        data = percent
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_circle_chart, container, false)
        circleChart2(data.toFloat(), view)
        return view

    }


    fun circleChart2(percent: Float, view: View) {


        val decoView = view.findViewById<DecoView>(R.id.dynamicArcView)

        val seriesItem = SeriesItem.Builder(ContextCompat.getColor(applicationContext, R.color.lightGrey))
            .setRange(0f, 100f, 0f)
            .build()

        val seriesItem2 = SeriesItem.Builder(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
            .setRange(0f, 100f, 0f)
            .build()

        val backIndex = decoView.addSeries(seriesItem)




        val textPercentage = view.findViewById<TextView>(R.id.textPercentage)
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

        decoView.addEvent(
            DecoEvent.Builder(100f)
                .setIndex(backIndex)
                .setDuration(1)
                .build()
        )

        if(percent == 0f) {
            view.findViewById<TextView>(R.id.textPercentage).text = "0%"
        } else {
            val series1Index = decoView.addSeries(seriesItem2)
            decoView.addEvent(
                DecoEvent.Builder(percent)
                    .setIndex(series1Index)
                    .build()
            )
        }

    }
}
