package com.example.domain.productadd

import com.example.domain.ProductsRepository
import com.example.domain.model.Product
import io.reactivex.Completable

class AddProductUseCase(private val repository: ProductsRepository) {
    fun add(product: Product): Completable = validate(product).andThen(repository.post(product))

    private fun validate(product: Product): Completable {
        return if (!product.isValidForAdd()) {
            Completable.error(IllegalArgumentException("product failed validation before add"))
        } else {
            Completable.complete()
        }
    }
}