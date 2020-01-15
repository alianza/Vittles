package com.example.data.room.productdictionary

import com.example.domain.barcode.ProductDictionary
import javax.inject.Inject

/**
 * Maps between Room database entity and model.
 *
 * @author Jeroen Flietstra
 */
class ProductDictionaryModelMapper @Inject constructor() {

    /**
     * Maps a product dictionary from entity to domain model.
     *
     * @param productDictionaryEntity The entity to be mapped.
     */
    fun fromEntity(productDictionaryEntity: ProductDictionaryEntity) =
        ProductDictionary(
            productDictionaryEntity.barcode,
            productDictionaryEntity.productName
        )

    /**
     * Maps a product dictionary from domain model to entity.
     *
     * @param productDictionary The model to be mapped.
     */
    fun toEntity(productDictionary: ProductDictionary) =
        ProductDictionaryEntity(
            productDictionary.barcode,
            productDictionary.productName
        )
}