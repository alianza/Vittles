package com.example.vittles.di

import android.content.Context
import com.example.data.*
import com.example.domain.repositories.ProductsApi
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
    fun provideProductsRepository(productDao: ProductDao, mapper: ProductModelMapper): ProductsRepository = ProductsRepositoryImpl(productDao, mapper)

    @Singleton
    @Provides
    fun provideProductsApi(): ProductsApi = ProductsApiImpl()
}