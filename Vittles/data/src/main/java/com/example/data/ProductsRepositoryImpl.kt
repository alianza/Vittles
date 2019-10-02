package com.example.data

import com.example.domain.ProductsRepository
import com.example.domain.model.Product
import io.reactivex.Completable
import io.reactivex.Single

/**
 * This is the implementation of the ProducsRepository in the Domain layer
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property productDaoImpl Reference to the ProductDaoImpl
 * @property mapper The mapper used to map the product data class
 */
class ProductsRepositoryImpl(private val productDaoImpl: ProductDaoImpl,
                             private val mapper: ProductModelMapperImpl) :
    ProductsRepository {

    override fun get(): Single<List<Product>> {
        var list = productDaoImpl.getAll()
            .map { it.map(mapper::fromEntity) }
        return list
    }

    override fun patch(product: Product): Completable = Completable.fromAction { productDaoImpl.insert(mapper.toEntity(product)) }

    override fun delete(product: Product): Completable = Completable.fromAction { productDaoImpl.delete(mapper.toEntity(product)) }

    override fun post(product: Product): Completable = Completable.fromAction { productDaoImpl.insert(mapper.toEntity(product)) }
}