package com.example.domain.model

import java.util.*

/*
Calender and msInDay are used for calculating the daysRemaining property.
msInDay represents the total of milliseconds in a day. This value is used to convert te remaining
milliseconds to remaining days.
*/
val calendar: Calendar = Calendar.getInstance()
const val msInDay: Double = (0.000000277777778).div(24)


data class Product(
    val uid: Int?,
    val productName: String?,
    val expirationDate: Date?,
    val creationDate: Date?,
    var indicationColor: Int?
) {
    fun getDaysRemaining(): Int {
        return (expirationDate!!.time.minus(calendar.time.time)).times(
            msInDay
        ).toInt()
    }

    // TODO: Change methods for validity
    fun isValidForEdit() = uid!! > 0 && productName!!.trim().length > 3

    fun isValidForAdd() = productName!!.trim().length > 3
}