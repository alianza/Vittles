package com.example.vittles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.vittles.R
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

/**
 * Product data model.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Arjen Simons
 *
 * @property uid unique id used as surrogate key.
 * @property productName name of the product.
 * @property expirationDate date of expiration.
 * @property creationDate date of when product was added to database.
 * @property daysRemaining days remaining until the expiration date elapses.
 * @property indicationColor color that indicates how close the product is to expiering.
 */
@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "product_name") val productName: String?,
    @ColumnInfo(name = "expiration_date") val expirationDate: Date?,
    @ColumnInfo(name = "creation_date") val creationDate: Date?
) {
    @Ignore var daysRemaining: Int = (expirationDate!!.time.minus(calendar.time.time)).times(msInDay).toInt()
    @Ignore var indicationColor: Int = when {
        daysRemaining < RED_COLOR_BOUNDARY -> R.color.red
        daysRemaining < YELLOW_COLOR_BOUNDARY -> R.color.yellow
        else -> R.color.green
    }
}
