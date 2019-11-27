package com.example.vittles.di

import android.content.Context
import com.example.data.*
import com.example.data.retrofit.off.OffApi
import com.example.data.retrofit.off.OffApiService
import com.example.data.retrofit.tsco.TscoApi
import com.example.data.retrofit.tsco.TscoApiService
import com.example.data.room.ProductDao
import com.example.data.room.ProductModelMapper
import com.example.data.room.WasteReportModelMapper
import com.example.data.room.createProductDaoImpl
import com.example.domain.repositories.ProductsRepository
import com.example.domain.repositories.WasteReportRepository
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
    fun provideTscoProductsApiService() = TscoApi.createApi()

    @Singleton
    @Provides
    fun provideOffProductsApiService() = OffApi.createApi()

    @Singleton
    @Provides
    fun provideProductsRepository(
        productDao: ProductDao,
        productsApiTSCO: TscoApiService,
        productsApiOFF: OffApiService,
        mapper: ProductModelMapper
    ): ProductsRepository = ProductsRepositoryImpl(productDao, productsApiTSCO, productsApiOFF, mapper)

    @Singleton
    @Provides
    fun provideWasteReportRepository(productDao: ProductDao, mapper: WasteReportModelMapper): WasteReportRepository = WasteReportRepositoryImpl(productDao, mapper)
}