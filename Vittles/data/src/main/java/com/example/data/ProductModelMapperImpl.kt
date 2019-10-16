package com.example.data

import com.example.domain.product.Product
import com.example.domain.repositories.ProductModelMapper
import javax.inject.Inject

/**
 * Maps between Room database entity and model.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 */
class ProductModelMapperImpl @Inject constructor() : ProductModelMapper<ProductEntity, Product> {
    override fun fromEntity(from: ProductEntity) = Product(from.uid, from.productName, from.expirationDate, from.creationDate, null)
    override fun toEntity(from: Product) = ProductEntity(from.uid, from.productName, from.expirationDate, from.creationDate)
}