package com.example.domain.barcode

import com.example.domain.repositories.ProductsRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class handles the business logic for barcode data.
 *
 * @author Jeroen Flietstra
 *
 * @property repository The products repository.
 */
class GetProductByBarcode @Inject constructor(private val repository: ProductsRepository) {

    /**
     * Calls the [ProductsRepository] to retrieve data from a barcode.
     *
     * @param barcode The barcode that has been scanned.
     * @return String value that represents the product description/name.
     */
    operator fun invoke(barcode: String): Observable<String> = repository.getProductNameByBarcode(barcode)
}