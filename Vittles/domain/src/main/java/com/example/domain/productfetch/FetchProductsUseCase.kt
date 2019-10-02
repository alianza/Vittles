package com.example.domain.productfetch

import com.example.domain.ProductsRepository
import com.example.domain.model.Product
import io.reactivex.Single

class FetchProductsUseCase(private val repository: ProductsRepository) {
    fun fetch(): Single<List<Product>> = repository.get()
}