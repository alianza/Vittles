package com.example.data

import com.example.domain.repositories.ProductsRepository
import com.example.domain.product.Product
import io.reactivex.Completable
import io.reactivex.Single

/**
 * This is the implementation of the ProductsRepository in the Domain layer.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property productDaoImpl Reference to the ProductDaoImpl.
 * @property mapper The mapper used to map the product data class.
 */
class ProductsRepositoryImpl(private val productDaoImpl: ProductDaoImpl,
                             private val mapper: ProductModelMapperImpl) :
    ProductsRepository {

    override fun get(): Single<List<Product>> {
        return productDaoImpl.getAll()
            .map { it.map(mapper::fromEntity) }
    }

    override fun patch(product: Product): Completable = Completable.fromAction { productDaoImpl.insert(mapper.toEntity(product)) }

    override fun delete(product: Product): Completable = Completable.fromAction { productDaoImpl.delete(mapper.toEntity(product)) }

    override fun post(product: Product): Completable = Completable.fromAction { productDaoImpl.insert(mapper.toEntity(product)) }
}