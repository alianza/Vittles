package com.example.domain

import com.example.domain.model.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductsRepository {

    fun get(): Single<List<Product>>

    fun patch(product: Product): Completable

    fun delete(product: Product): Completable

    fun post(product: Product): Completable
}