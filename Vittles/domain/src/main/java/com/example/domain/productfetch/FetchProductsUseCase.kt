package com.example.domain.productfetch

import com.example.domain.repositories.ProductsRepository
import com.example.domain.model.Product
import io.reactivex.Single
import javax.inject.Inject

/**
 * This class handles the business logic of fetching all the products received from the local database.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property repository The ProductsRepository.
 */
class FetchProductsUseCase @Inject constructor(private val repository: ProductsRepository) {
    /**
     * Gets all the products from the local database.
     *
     * @return A list containing all the products.
     */
    fun fetch(): Single<List<Product>> = repository.get()
}