package com.example.domain.model

import java.util.*

/*
Calender and msInDay are used for calculating the daysRemaining property.
msInDay represents the total of milliseconds in a day. This value is used to convert te remaining
milliseconds to remaining days.
*/
val calendar: Calendar = Calendar.getInstance()
const val msInDay: Double = (0.000000277777778).div(24)
const val RED_COLOR_BOUNDARY: Int = 3
const val YELLOW_COLOR_BOUNDARY: Int = 7

data class Product(
    val uid: Int?,
    val productName: String?,
    val expirationDate: Date?,
    val creationDate: Date?
) {
    fun getDaysRemaining(): Int {
        return (expirationDate!!.time.minus(calendar.time.time)).times(
            msInDay
        ).toInt()
    }

    fun getIndicationColor(): Int {
//        var daysRemaining = getDaysRemaining()
//        var indicationColor: Int = when {
//            daysRemaining < RED_COLOR_BOUNDARY -> R.color.red
//            daysRemaining < YELLOW_COLOR_BOUNDARY -> R.color.yellow
//            else -> R.color.green
//        }
        return 1
    }

    // TODO: Change methods for validity
    fun isValidForEdit() = uid!! > 0 && productName!!.trim().length > 3

    fun isValidForAdd() = productName!!.trim().length > 3
}