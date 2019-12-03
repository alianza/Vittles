package com.example.domain.barcode

import com.example.domain.repositories.BarcodesRepository
import io.reactivex.Completable
import javax.inject.Inject

/**
 * This class handles the business logic for barcode data updating.
 *
 * @author Jeroen Flietstra
 *
 * @property repository The products repository.
 */
class UpdateBarcodeDictionary @Inject constructor(private val repository: BarcodesRepository) {

    /**
     * Calls the repository to update a barcode dictionary.
     *
     * @param productDictionary The barcode dictionary to insert.
     * @return Completable to indicate if the action has succeeded.
     */
    operator fun invoke(productDictionary: ProductDictionary): Completable =
        repository.updateBarcodeDictionaryRoom(productDictionary)
}