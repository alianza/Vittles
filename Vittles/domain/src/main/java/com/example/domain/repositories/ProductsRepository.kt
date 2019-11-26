package com.example.domain.repositories

import com.example.domain.exceptions.ProductNotFoundException
import com.example.domain.product.Product
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Repository interface for the products witch is implemented in the data module.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 */
interface ProductsRepository {

    /**
     * Gets all the products from the local database.
     *
     * @return A list containing all the products.
     */
    fun get(): Single<List<Product>>

    /**
     * Updates a product in the database.
     *
     * @param product The product to update.
     * @return A completable status.
     */
    fun patch(product: Product): Completable

    /**
     * Deletes a product in the database.
     *
     * @param product The product to delete.
     * @return A completable status.
     */
    fun delete(product: Product): Completable

    /**
     * Adds a product in the database.
     *
     * @param product The product to invoke.
     * @return A completable status.
     */
    fun post(product: Product): Completable

    /**
     * Calls external API to look for product name of the barcode.
     *
     * @param barcode The barcode to look up.
     * @return Observable string value of the product name.
     */
    @Throws(ProductNotFoundException::class)
    fun getProductNameByBarcodeTSCO(barcode: String): Observable<String>

    /**
     * Calls external API to look for product name of the barcode.
     *
     * @param barcode The barcode to look up.
     * @return Observable string value of the product name.
     */
    @Throws(ProductNotFoundException::class)
    fun getProductNameByBarcodeOFF(barcode: String): Observable<String>
}