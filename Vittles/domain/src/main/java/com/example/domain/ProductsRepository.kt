package com.example.domain

import com.example.domain.model.Product

interface ProductsRepository {

    fun get(): List<Product>

    fun patch(product: Product): Int

    fun delete(uid: Int): Int

    fun post(product: Product): Int
}