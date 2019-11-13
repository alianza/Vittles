package com.example.vittles.wastereport

import com.example.domain.wasteReport.GetCountEatenProducts
import com.example.domain.wasteReport.GetCountExpiredProducts
import com.example.domain.wasteReport.GetWastePercent
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

class WasteReportPresenter @Inject internal constructor(
    private val getCountEatenProducts: GetCountEatenProducts,
    private val getCountExpiredProducts: GetCountExpiredProducts,
    private val getWastePercent: GetWastePercent) :
    BasePresenter<WasteReportFragment>(), WasteReportContract.Presenter {


    override fun getCountEatenProducts(date: DateTime) {
        disposables.add(getCountEatenProducts.invoke(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.showEatenProducts(it) }, { view?.setNoResultsView() })
        )
    }

    override fun getCountExpiredProducts(date: DateTime) {
        disposables.add(getCountExpiredProducts.invoke(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.showExpiredProducts(it) }, { view?.setNoResultsView() })
        )
    }


}