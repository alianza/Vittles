package com.example.data

import android.content.Context
import com.example.domain.ProductsRepository
import com.example.domain.model.Product

class ProductsRepositoryImpl private constructor(context: Context) : ProductsRepository {

    private var productDao: ProductDao = AppDatabase.getDatabase(context).productDao()
    private lateinit var instance: ProductsRepositoryImpl

    companion object {
        val instance = ProductsRepositoryImpl()
    }

    override fun get(): List<Product> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun patch(product: Product): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uid: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun post(product: Product): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}