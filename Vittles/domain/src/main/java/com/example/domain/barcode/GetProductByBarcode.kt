package com.example.domain.barcode

import com.example.domain.repositories.ProductsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created for future development.
 *
 * @property repository
 */
class GetProductByBarcode @Inject constructor(private val repository: ProductsRepository) {

    operator fun invoke(barcode: String): Single<String> = repository.getProductNameByBarcode(barcode)
}