package com.example.vittles.enums

import org.joda.time.DateTime

enum class TimeRangeSteps(val steps: Int) {
    SEVEN_DAYS(7),
    THIRTY_DAYS(30),
    LAST_YEAR(365),
    MONTH_YEAR(12)
}