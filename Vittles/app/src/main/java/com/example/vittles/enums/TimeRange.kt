package com.example.vittles.enums

import org.joda.time.DateTime

/**
 * Enumerator which holds the date of time ranges
 *
 * @author Sarah Lange
 *
 * @property date date where the time range starts
 */
enum class TimeRange(val date: DateTime) {
    /** Time span for the last week. **/
    LAST_SEVEN_DAYS(DateTime.now().minusDays(7).withTimeAtStartOfDay()),
    /** Time span for the last month/ **/
    LAST_30_DAYS(DateTime.now().minusDays(30).withTimeAtStartOfDay()),
    /** Time span for the last year. **/
    LAST_YEAR(DateTime.now().minusDays(365).withTimeAtStartOfDay())
}