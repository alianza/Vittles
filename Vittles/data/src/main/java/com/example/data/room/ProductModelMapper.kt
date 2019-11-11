package com.example.data.room

import com.example.domain.product.Product
import javax.inject.Inject

/**
 * Maps between Room database entity and model.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 */
class ProductModelMapper @Inject constructor() {
    fun fromEntity(from: ProductEntity) = Product(from.uid, from.productName, from.expirationDate, from.creationDate, null)
    fun toEntity(from: Product) = ProductEntity(
        from.uid,
        from.productName,
        from.expirationDate,
        from.creationDate
    )
}