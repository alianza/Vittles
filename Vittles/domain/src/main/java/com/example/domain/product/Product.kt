package com.example.domain.product

import org.joda.time.*


/**
 * Represents the total amount of milliseconds in a day. This value is used to convert the remaining milliseconds to remaining days.
 */

/**
 * The product entity.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property uid The Id of the product (used as primary key in the database.
 * @property productName The name of the product.
 * @property expirationDate The expiration date of the product.
 * @property creationDate The date the product was added to the application.
 * @property indicationColor The color that indicates how close a product is till expiring.
 */
data class Product(
    val uid: Int?,
    val productName: String?,
    val expirationDate: DateTime?,
    val creationDate: DateTime?,
    var indicationColor: Int?
) {
    /**
     * Calculates the amount of days until the product expires.
     *
     * @return An integer with the amount of days left.
     */
    fun getDaysRemaining(): Int {
        return Days.daysBetween(DateTime.now().withTimeAtStartOfDay(), expirationDate).days
    }

    /**
     * Checks if the change to a product is valid.
     *
     */
    fun isValidForEdit() = uid!! > 0 && productName!!.trim().isNotEmpty() && expirationDate.toString().trim().isNotEmpty()

    /**
     * Checks if the input for a product is valid.
     *
     */
    fun isValidForAdd() = productName!!.trim().isNotEmpty() && expirationDate.toString().trim().isNotEmpty()
}