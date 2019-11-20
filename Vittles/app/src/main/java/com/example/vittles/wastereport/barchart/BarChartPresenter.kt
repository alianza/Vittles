package com.example.vittles.wastereport.barchart

import com.example.domain.wasteReport.GetWastePercent
import com.example.vittles.mvp.BasePresenter
import javax.inject.Inject

class BarChartPresenter @Inject internal constructor(private val getWastePercent: GetWastePercent) :
    BasePresenter<BarChartFragment>(), BarChartContract.Presenter {

}