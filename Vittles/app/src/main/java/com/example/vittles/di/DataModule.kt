package com.example.vittles.di

import android.content.Context
import com.example.data.*
import com.example.domain.repositories.ProductsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideProductDaoImpl(context: Context) = createProductDaoImpl(context)

    @Singleton
    @Provides
    fun provideProductsApiService() = ProductsApiImpl.createApi()

    @Singleton
    @Provides
    fun provideProductsRepository(
        productDao: ProductDao,
        productsApi: ProductsApiService,
        mapper: ProductModelMapper
    ): ProductsRepository = ProductsRepositoryImpl(productDao, productsApi, mapper)
}