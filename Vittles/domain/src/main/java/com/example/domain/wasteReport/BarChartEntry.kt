package com.example.domain.wasteReport

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat

data class BarChartEntry(
    val vittlesEatenPercent: Float,
    val vittlesExpiredPercent: Float,
    val deleteDate: DateTime
) {

    fun getWeekday(): String {
        val fmt = DateTimeFormat.forPattern("EEE")
        return deleteDate.toString(fmt)
    }
}