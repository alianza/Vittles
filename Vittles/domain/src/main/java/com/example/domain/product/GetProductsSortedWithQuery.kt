package com.example.domain.product

import com.example.domain.product.model.Product
import com.example.domain.product.model.ProductSortingType
import io.reactivex.Observable
import javax.inject.Inject

class GetProductsSortedWithQuery @Inject constructor(private val repository: ProductsRepository) {

    operator fun invoke(sortingType: ProductSortingType, query: String = ""): Observable<List<Product>> =
        repository.getAllSortedByWithQuery(sortingType, query)
}