package com.example.data

import com.example.domain.model.Product
import com.example.domain.repositories.ProductModelMapper

/**
 * Maps between Room database entity and model.
 */
class ProductModelMapperImpl : ProductModelMapper<ProductEntity, Product> {
    override fun fromEntity(from: ProductEntity) = Product(from.uid, from.productName, from.expirationDate, from.creationDate)
    override fun toEntity(from: Product) = ProductEntity(from.uid, from.productName, from.expirationDate, from.creationDate)
}