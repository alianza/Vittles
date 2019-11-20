package com.example.vittles.di

import android.content.Context
import com.example.data.*
import com.example.data.retrofit.ProductsApi
import com.example.data.retrofit.ProductsApiService
import com.example.data.room.ProductDao
import com.example.data.room.ProductModelMapper
import com.example.data.room.createProductDaoImpl
import com.example.domain.repositories.ProductsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/** @suppress */
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideProductDaoImpl(context: Context) =
        createProductDaoImpl(context)

    @Singleton
    @Provides
    fun provideProductsApiService() = ProductsApi.createApi()

    @Singleton
    @Provides
    fun provideProductsRepository(
        productDao: ProductDao,
        productsApi: ProductsApiService,
        mapper: ProductModelMapper
    ): ProductsRepository = ProductsRepositoryImpl(productDao, productsApi, mapper)
}