package com.example.domain.product.model

import org.joda.time.*

data class Product(
    val uid: Int,
    val productName: String,
    val expirationDate: DateTime,
    val creationDate: DateTime,
    val barcode: String?
) {

    fun getDaysRemaining(): Int {
        return Days.daysBetween(DateTime.now().withTimeAtStartOfDay(), expirationDate).days
    }

    fun isValidForEdit() = uid > 0  && productName.trim().isNotEmpty() && expirationDate.toString().trim().isNotEmpty()

    fun isValidForAdd() = productName.trim().isNotEmpty() && expirationDate.toString().trim().isNotEmpty()
}