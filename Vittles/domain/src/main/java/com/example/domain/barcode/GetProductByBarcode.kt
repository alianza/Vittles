package com.example.domain.barcode

import com.example.domain.repositories.ProductsRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created for future development.
 *
 * @property repository
 */
class GetProductByBarcode @Inject constructor(private val repository: ProductsRepository) {

    operator fun invoke(barcode: String): Observable<String> = repository.getProductNameByBarcode(barcode)
}