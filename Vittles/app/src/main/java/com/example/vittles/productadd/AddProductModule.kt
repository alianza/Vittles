package com.example.vittles.productadd

import com.example.domain.ProductsRepository
import com.example.domain.productadd.AddProductUseCase
import dagger.Module
import dagger.Provides

/**
 * Module for AddProduct dependency injections.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 */
@Module
class AddProductModule {

    /**
     * Provides the AddProductUseCase as injection. Generated by Dagger framework.
     *
     * @param productsRepository ProductsRepository needed for the AddProductUseCase.
     */
    @Provides
    fun provideAddProductUseCase(productsRepository: ProductsRepository) = AddProductUseCase(productsRepository)

    /**
     * Provides the AddProductPresenter as injection. Generated by Dagger Framework.
     *
     * @param addProductUseCase AddProductUseCase needed for the AddProductPresenter.
     */
    @Provides
    fun providePresenter(addProductUseCase: AddProductUseCase) =
        AddProductPresenter(addProductUseCase)
}