package com.example.domain.repositories

import com.example.domain.barcode.ProductDictionary
import com.example.domain.exceptions.ProductNotFoundException
import io.reactivex.Completable
import io.reactivex.Observable

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
    fun getProductNameByBarcodeTSCO(barcode: String): Observable<ProductDictionary>

    /**
     * Calls external API to look for product name of the barcode.
     *
     * @param barcode The barcode to look up.
     * @return Observable string value of the product name.
     */
    @Throws(ProductNotFoundException::class)
    fun getProductNameByBarcodeOFF(barcode: String): Observable<ProductDictionary>

    /**
     * Retrieve product name by barcode from local database.
     *
     * @param barcode The search query.
     * @return The product name.
     */
    fun getProductNameByBarcodeRoom(barcode: String): Observable<ProductDictionary>

    /**
     * Insert product dictionary into the local database.
     *
     * @param productDictionary The dictionary to insert.
     * @return Completable that represents if the insert has succeeded.
     */
    fun insertProductDictionaryRoom(productDictionary: ProductDictionary): Completable

    /**
     * Update product dictionary in the local database.
     *
     * @param productDictionary The dictionary to update.
     * @return Completable that represents if the insert has succeeded.
     */
    fun updateProductDictionaryRoom(productDictionary: ProductDictionary): Completable

    /**
     * Delete product dictionaries in the local database.
     *
     * @return Completable that represents if the insert has succeeded.
     */
    fun emptyProductDictionaryRoom(): Completable
}