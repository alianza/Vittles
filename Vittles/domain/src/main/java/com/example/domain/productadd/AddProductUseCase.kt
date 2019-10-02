package com.example.domain.productadd

import com.example.domain.ProductsRepository
import com.example.domain.model.Product
import io.reactivex.Completable

/**
 * This class handles te business logic of adding a new product to the application.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property repository The productsRepository.
 */
class AddProductUseCase(private val repository: ProductsRepository) {

    /**
     * This method is used to add a product to the database.
     *
     * @param product The product that will be added.
     * @return The compatibility status of adding the product ot the database.
     */
    fun add(product: Product): Completable = validate(product).andThen(repository.post(product))

    /**
     * Validates if the product can be added to the database.
     *
     * @param product The product that will be added to the database.
     * @return The compatibility status.
     */
    private fun validate(product: Product): Completable {
        return if (!product.isValidForAdd()) {
            Completable.error(IllegalArgumentException("product failed validation before add"))
        } else {
            Completable.complete()
        }
    }
}