package com.example.vittles.dashboard.model

import android.os.Parcelable
import com.example.domain.consts.DAYS_REMAINING_BOUNDARY_CLOSE
import com.example.domain.consts.DAYS_REMAINING_EXPIRED
import com.example.vittles.enums.DeleteType
import com.example.vittles.enums.IndicationColor
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

@Parcelize
data class ProductViewModel(
    var uid: Int,
    var productName: String,
    var expirationDate: DateTime,
    val creationDate: DateTime,
    val daysRemaining: Int,
    var deleteType: DeleteType?
) : Parcelable {

    fun getIndicationColor(): IndicationColor {
        return when {
            daysRemaining < DAYS_REMAINING_EXPIRED -> IndicationColor.RED
            daysRemaining < DAYS_REMAINING_BOUNDARY_CLOSE -> IndicationColor.YELLOW
            else -> IndicationColor.GREEN
        }
    }
}