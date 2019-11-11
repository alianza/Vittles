package com.example.vittles.scanning

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

@Parcelize
data class ScanResult (
    val productName: String?,
    val expirationDate: DateTime?
) : Parcelable