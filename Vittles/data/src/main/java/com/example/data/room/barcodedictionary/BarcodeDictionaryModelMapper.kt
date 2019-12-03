package com.example.data.room.barcodedictionary

import com.example.domain.barcode.BarcodeDictionary
import javax.inject.Inject

/**
 * Maps between Room database entity and model.
 *
 * @author Jeroen Flietstra
 */
class BarcodeDictionaryModelMapper @Inject constructor() {

    fun fromEntity(barcodeDictionaryEntity: BarcodeDictionaryEntity) =
        BarcodeDictionary(
            barcodeDictionaryEntity.barcode.toString(),
            barcodeDictionaryEntity.productName
        )

    fun toEntity(barcodeDictionary: BarcodeDictionary) =
        BarcodeDictionaryEntity(
            barcodeDictionary.barcode.toLong(),
            barcodeDictionary.productName
        )
}