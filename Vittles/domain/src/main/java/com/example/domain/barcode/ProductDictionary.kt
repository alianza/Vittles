package com.example.domain.barcode

import com.example.domain.enums.BarcodeDictionaryStatus

/**
 * Dictionary model for barcode (key), product name (value) pairs.
 *
 * @author Jeroen Flietstra
 *
 * @property barcode The key.
 * @property productName The value.
 */
data class ProductDictionary(
    val barcode: String,
    val productName: String?
) {
    fun containsNotReady(): Boolean {
        return barcode == BarcodeDictionaryStatus.NOT_READY() || productName == BarcodeDictionaryStatus.NOT_READY()
    }

    fun containsNotFound(): Boolean {
        return barcode == BarcodeDictionaryStatus.NOT_FOUND() || productName == BarcodeDictionaryStatus.NOT_FOUND()
    }
}