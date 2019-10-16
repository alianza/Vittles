package com.example.domain.productdelete

import com.example.domain.model.Product
import com.example.domain.repositories.ProductsRepository
import io.reactivex.Completable

/**
 * This class handles te business logic of deleting a product from the application.
 *
 * @author Sarah Lange
 *
 * @property repository The productsRepository.
 */
class DeleteProductUseCase(private val repository: ProductsRepository) {

    /**
     * This method is used to delete a product from the database.
     *
     * @param product The product that will be deleted.
     * @return The compatibility status of deleting product from the database.
     */
    fun delete(product: Product): Completable = repository.delete(product)


}