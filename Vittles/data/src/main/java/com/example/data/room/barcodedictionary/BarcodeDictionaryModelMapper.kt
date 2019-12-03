package com.example.data.room.barcodedictionary

import com.example.domain.barcode.ProductDictionary
import javax.inject.Inject

/**
 * Maps between Room database entity and model.
 *
 * @author Jeroen Flietstra
 */
class BarcodeDictionaryModelMapper @Inject constructor() {

    fun fromEntity(barcodeDictionaryEntity: BarcodeDictionaryEntity) =
        ProductDictionary(
            barcodeDictionaryEntity.barcode.toString(),
            barcodeDictionaryEntity.productName
        )

    fun toEntity(productDictionary: ProductDictionary) =
        BarcodeDictionaryEntity(
            productDictionary.barcode.toLong(),
            productDictionary.productName
        )
}