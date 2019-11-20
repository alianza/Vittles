package com.example.vittles.enums

import com.example.vittles.R
import org.joda.time.DateTime
import java.util.*

enum class TimeRange(val value: DateTime) {
    LAST_SEVEN_DAYS(DateTime.now().minusDays(7)),
    LAST_30_DAYS(DateTime.now().minusDays(30)),
    LAST_YEAR(DateTime.now().minusDays(365))
}