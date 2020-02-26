package com.example.vittles.wastereport

import android.content.Context
import com.example.vittles.R
import com.example.vittles.enums.TimeRange
import com.example.vittles.enums.TimeRange.*
import com.example.vittles.services.popups.OptionTextProvider

/**
 * @author Jeroen Flietstra
 *
 */
class TimeRangeTextProvider(
    private val context: Context
) : OptionTextProvider<TimeRange> {

    override fun getText(option: TimeRange): String {
        return when(option) {
            LAST_SEVEN_DAYS -> context.getString(R.string.last_seven_days)
            LAST_30_DAYS -> context.getString(R.string.last_30_days)
            LAST_YEAR -> context.getString(R.string.last_year)
        }
    }
}