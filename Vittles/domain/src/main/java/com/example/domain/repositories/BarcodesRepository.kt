package com.example.domain.repositories

import com.example.domain.barcode.BarcodeDictionary
import com.example.domain.exceptions.ProductNotFoundException
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Repository interface for the barcode dictionary.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
interface BarcodesRepository {

    /**
     * Calls external API to look for product name of the barcode.
     *
     * @param barcode The barcode to look up.
     * @return Observable string value of the product name.
     */
    @Throws(ProductNotFoundException::class)
    fun getProductNameByBarcodeTSCO(barcode: String): Observable<BarcodeDictionary>

    /**
     * Calls external API to look for product name of the barcode.
     *
     * @param barcode The barcode to look up.
     * @return Observable string value of the product name.
     */
    @Throws(ProductNotFoundException::class)
    fun getProductNameByBarcodeOFF(barcode: String): Observable<BarcodeDictionary>

    /**
     * Retrieve product name by barcode from local database.
     *
     * @param barcode The search query.
     * @return The product name.
     */
    fun getProductNameByBarcodeRoom(barcode: String): Observable<BarcodeDictionary>

    /**
     * Insert barcode dictionary into the local database.
     *
     * @param barcodeDictionary The dictionary to insert.
     * @return Completable that represents if the insert has succeeded.
     */
    fun insertBarcodeDictionaryRoom(barcodeDictionary: BarcodeDictionary): Completable

    /**
     * Update barcode dictionary in the local database.
     *
     * @param barcodeDictionary The dictionary to update.
     * @return Completable that represents if the insert has succeeded.
     */
    fun updateBarcodeDictionaryRoom(barcodeDictionary: BarcodeDictionary): Completable
}