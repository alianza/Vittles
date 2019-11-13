package com.example.vittles.wastereport

import org.joda.time.DateTime


/**
 * MVP Contract for waste report.
 *
 * @author Sarah Lange
 */
interface WasteReportContract {

    interface View {
        fun initViews()
        fun addOnPageChangeListener()
        fun showEatenProducts(eatenProducts: Int)
        fun setNoResultsView()
        fun showExpiredProducts(expiredProducts: Int)
        fun calculateWaste(percent: Int)
        fun showTimeRangeSelector()
        fun changeDate(date: DateTime)
    }

    interface Presenter {
        fun getCountEatenProducts(date: DateTime)
        fun getCountExpiredProducts(date: DateTime)
    }
}