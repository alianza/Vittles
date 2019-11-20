package com.example.vittles.wastereport

import org.joda.time.DateTime


/**
 * MVP Contract for waste report.
 *
 * @author Sarah Lange
 */
interface WasteReportContract {

    interface View {
        fun initViews(vittlesEaten: Int, vittlesExpired: Int)
        fun initData()
        fun addOnPageChangeListener()
        fun showChangeDate(vittlesEaten: Int, vittlesExpired: Int, date: DateTime)
        fun showEatenVittles(eatenProducts: Int)
        fun setNoResultsView()
        fun showExpiredVittles(expiredProducts: Int)
        fun showTimeRangeSelector()
        fun changeDate(date: DateTime)
    }

    interface Presenter {
        fun getCountEatenProducts(date: DateTime): Int
        fun getCountExpiredProducts(date: DateTime): Int
    }
}