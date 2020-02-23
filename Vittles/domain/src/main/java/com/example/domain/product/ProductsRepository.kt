package com.example.domain.product

import com.example.domain.product.model.Product
import com.example.domain.product.model.ProductSortingType
import io.reactivex.Completable
import io.reactivex.Observable

interface ProductsRepository {

    fun get(): Observable<List<Product>>

    fun getAllSortedByWithQuery(sortingType: ProductSortingType, query: String): Observable<List<Product>>

    fun patch(product: Product): Completable

    fun delete(product: Product): Completable

    fun post(product: Product): Completable
}