package com.example.vittles.di

import android.content.Context
import com.example.data.ProductDaoImpl
import com.example.data.ProductModelMapperImpl
import com.example.data.ProductsRepositoryImpl
import com.example.data.createProductDaoImpl
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
    fun provideProductsRepository(productDaoImpl: ProductDaoImpl, mapper: ProductModelMapperImpl): ProductsRepository = ProductsRepositoryImpl(productDaoImpl, mapper)
}