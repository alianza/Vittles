package com.example.data.room.product

import com.example.domain.product.model.Product
import com.example.domain.product.ProductsRepository
import com.example.domain.product.model.ProductSortingType
import com.example.domain.product.model.ProductSortingType.*
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

class ProductsRepositoryImpl(
    private val productDao: ProductDao,
    private val mapper: ProductModelMapper
) :
    ProductsRepository {

    override fun get(): Observable<List<Product>> {
        return productDao.getAll()
            .map { it.map(mapper::fromEntity) }
    }

    override fun getAllSortedByWithQuery(sortingType: ProductSortingType, query: String): Observable<List<Product>> {
        val preparedQuery = "%${query.toLowerCase(Locale.getDefault())}%"
        return when (sortingType) {
            DAYS_REMAINING_ASC -> productDao.getAllByExpirationDateAscWithQuery(preparedQuery).map { it.map(mapper::fromEntity) }
            DAYS_REMAINING_DESC -> productDao.getAllByExpirationDateDescWithQuery(preparedQuery).map { it.map(mapper::fromEntity) }
            ALPHABETIC_ASC -> productDao.getAllByProductNameAscWithQuery(preparedQuery).map { it.map(mapper::fromEntity) }
            ALPHABETIC_DESC -> productDao.getAllByProductNameDescWithQuery(preparedQuery).map { it.map(mapper::fromEntity) }
            CREATION_DATE_ASC -> productDao.getAllByCreationDateAscWithQuery(preparedQuery).map { it.map(mapper::fromEntity) }
            CREATION_DATE_DESC ->  productDao.getAllByCreationDateDescWithQuery(preparedQuery).map { it.map(mapper::fromEntity) }
        }
    }

    override fun patch(product: Product): Completable =
        Completable.fromAction { productDao.update(mapper.toEntity(product)) }

    override fun delete(product: Product): Completable =
        Completable.fromAction { productDao.delete(mapper.toEntity(product)) }

    override fun post(product: Product): Completable =
        Completable.fromAction { productDao.insert(mapper.toEntity(product)) }
}