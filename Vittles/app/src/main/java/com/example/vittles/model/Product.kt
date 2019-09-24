package com.example.vittles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

/*
Calender and msInDay are used for calculating the daysRemaining property.
msInDay represents the total of milliseconds in a day. This value is used to convert te remaining
milliseconds to remaining days.
 */
val calendar: Calendar = Calendar.getInstance()
const val msInDay: Double = (0.000000277777778).div(24)

/**
 * Product data model.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 *
 * @property uid unique id used as surrogate key.
 * @property productName name of the product.
 * @property expirationDate date of expiration.
 * @property creationDate date of when product was added to database.
 * @property daysRemaining days remaining until the expiration date elapses.
 */
@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "product_name") val productName: String?,
    @ColumnInfo(name = "expiration_date") val expirationDate: Date?,
    @ColumnInfo(name = "creation_date") val creationDate: Date?
) {
    @Ignore var daysRemaining: Int = (expirationDate!!.time.minus(calendar.time.time)).times(msInDay).toInt()
}
