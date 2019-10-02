package com.example.vittles.di

import android.content.Context
import com.example.data.ProductDaoImpl
import com.example.data.ProductModelMapperImpl
import com.example.data.ProductsRepositoryImpl
import com.example.data.createProductDao
import com.example.domain.ProductsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDbModule {

    @Singleton
    @Provides
    fun provideNoteDao(context: Context) = createProductDao(context)

    @Singleton
    @Provides
    fun provideProductModelMapper() = ProductModelMapperImpl()

    @Singleton
    @Provides
    fun provideProductsRepository(productDaoImpl: ProductDaoImpl, mapper: ProductModelMapperImpl): ProductsRepository = ProductsRepositoryImpl(productDaoImpl, mapper)
}