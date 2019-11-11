package com.example.data

import com.example.domain.repositories.ProductsRepository
import com.example.domain.product.Product
import com.example.domain.repositories.ProductsApi
import io.reactivex.Completable
import io.reactivex.Single

/**
 * This is the implementation of the ProductsRepository in the Domain layer.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property productDao Reference to the ProductDao.
 * @property mapper The mapper used to map the product data class.
 */
class ProductsRepositoryImpl(private val productDao: ProductDao,
                             private val productsApi: ProductsApiService,
                             private val mapper: ProductModelMapper) :
    ProductsRepository {

    override fun get(): Single<List<Product>> {
        return productDao.getAll()
            .map { it.map(mapper::fromEntity) }
    }

    override fun patch(product: Product): Completable = Completable.fromAction { productDao.insert(mapper.toEntity(product)) }

    override fun delete(product: Product): Completable = Completable.fromAction { productDao.delete(mapper.toEntity(product)) }

    override fun post(product: Product): Completable = Completable.fromAction { productDao.insert(mapper.toEntity(product)) }

    override fun getProductNameByBarcode(barcode: String): Single<String> {
        return productsApi.getProductName(barcode).
    }
}