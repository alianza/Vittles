package com.example.vittles.di

import android.content.Context
import com.example.data.*
import com.example.domain.repositories.ProductsRepository
import com.example.domain.repositories.WasteReportRepository
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
    fun provideWasteReportRepository(productDao: ProductDao, mapper: WasteReportModelMapper): WasteReportRepository = WasteReportRepositoryImpl(productDao, mapper)
}