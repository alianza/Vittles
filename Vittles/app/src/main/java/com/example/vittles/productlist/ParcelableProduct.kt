package com.example.vittles.productlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

/**
 * A parcelable variant of the product entity.
 *
 * @author Arjen Simons
 *
 * @property uid The product id.
 * @property productName The product name.
 * @property expirationDate The product expiration date.
 * @property creationDate The date the product was added to the room database.
 */
@Parcelize
data class ParcelableProduct (
    val uid: Int?,
    val productName: String,
    val expirationDate: DateTime,
    val creationDate: DateTime
    ) : Parcelable