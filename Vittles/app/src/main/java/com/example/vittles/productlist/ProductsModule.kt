package com.example.vittles.productlist

import com.example.domain.ProductsRepository
import com.example.domain.productfetch.FetchProductsUseCase
import dagger.Module
import dagger.Provides

@Module
class ProductsModule {

    @Provides
    fun provideProductsUseCase(productsRepository: ProductsRepository) = FetchProductsUseCase(productsRepository)


    @Provides
    fun providePresenter(fetchProductsUseCase: FetchProductsUseCase) = ProductsPresenter(fetchProductsUseCase)
}