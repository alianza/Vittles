package com.example.vittles.productadd

import com.example.domain.ProductsRepository
import com.example.domain.productadd.AddProductUseCase
import dagger.Module
import dagger.Provides

@Module
class AddProductModule {

    @Provides
    fun provideAddProductUseCase(productsRepository: ProductsRepository) = AddProductUseCase(productsRepository)

    @Provides
    fun providePresenter(addProductUseCase: AddProductUseCase) = AddProductPresenter(addProductUseCase)
}