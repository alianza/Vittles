package com.example.domain.barcode

import com.example.domain.repositories.BarcodesRepository
import io.reactivex.Completable
import javax.inject.Inject

/**
 * This class handles the business logic for barcode data insertion.
 *
 * @author Jeroen Flietstra
 *
 * @property repository The products repository.
 */
class AddBarcodeDictionary @Inject constructor(private val repository: BarcodesRepository) {

    /**
     * Calls the repository to insert a barcode dictionary.
     *
     * @param productDictionary The barcode dictionary to insert.
     * @return Completable to indicate if the action has succeeded.
     */
    operator fun invoke(productDictionary: ProductDictionary): Completable = repository.insertBarcodeDictionaryRoom(productDictionary)
}