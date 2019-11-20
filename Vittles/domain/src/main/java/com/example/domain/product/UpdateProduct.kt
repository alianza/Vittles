package com.example.domain.product

import com.example.domain.repositories.ProductsRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateProduct @Inject constructor(private val repository: ProductsRepository) {

    fun invoke(product: Product): Completable = validate(product).andThen(repository.patch(product))

    fun validate(product: Product): Completable{
        return if(product.isValidForEdit())
    }
}