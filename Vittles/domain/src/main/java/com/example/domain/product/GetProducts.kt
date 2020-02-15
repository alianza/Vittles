package com.example.domain.product

import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class handles the business logic of fetching all the products received from the local database.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property repository The ProductsRepository.
 */
class GetProducts @Inject constructor(private val repository: ProductsRepository) {
    /**
     * Gets all the products from the local database.
     *
     * @return A list containing all the products.
     */
    operator fun invoke(): Observable<List<Product>> = repository.get()
}