package com.example.vittles.wastereport.CircleChart

import com.example.domain.wasteReport.GetWastePercent
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

class CircleChartPresenter @Inject internal constructor( private val getWastePercent: GetWastePercent) :
    BasePresenter<CircleChartFragment>(), CircleChartContract.Presenter {


    override fun getEatenPercent(date: DateTime, vittlesEaten: Int, vittlesExpired: Int) {
        disposables.add(getWastePercent.invoke(vittlesEaten, vittlesExpired)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.drawCircleChart(it) }, { view?.setNoResultsView() })
        )
    }
}