package com.example.vittles.wastereport.circlechart

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

/**
 * Interface for updating date
 *
 * @author Sarah Lange
 *
 */
interface RefreshData {

    /**
     * Refreshes data after setting new date
     *
     * @param date From this date up to now the date is calculated
     * @param vittlesEaten Amount of eaten vittles
     * @param vittlesExpired Amount of expired vittles
     */
    fun refresh(date: DateTime, vittlesEaten: Int, vittlesExpired: Int)
}

/**
 * Fragment class for the waste report.
 *
 * @property date From this date up to now the date is calculated
 * @property vittlesEaten Amount of eaten vittles
 * @property vittlesExpired Amount of expired vittles
 */
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

    /**
     * Implementation of the refresh interface
     *
     * @param date New date from which the date is calculated
     * @param vittlesEaten New amount of eaten vittles
     * @param vittlesExpired New amount of expired vittles
     */
    override fun refresh(date: DateTime, vittlesEaten: Int, vittlesExpired: Int) {
        this.date = date
        this.vittlesEaten = vittlesEaten
        this.vittlesExpired = vittlesExpired
        presenter.getEatenPercent(date, vittlesEaten, vittlesExpired)
    }

    /**
     * Draws circle chart
     *
     * @param percent percent date
     */
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

    /**
     * Shows toast if an error occurs
     *
     */
    override fun setNoResultsView() {
        Toast.makeText(context, "Error Cicrle Chart", Toast.LENGTH_SHORT).show()
    }

}
