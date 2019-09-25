package com.example.vittles.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.vittles.model.Product

/**
 * Interface dao for CRUD actions on the product data model.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
@Dao
interface ProductDao {

    /**
     * Retrieves all products from the database.
     *
     * @return list of products as a result from the query.
     */
    @Query("SELECT * FROM product ORDER BY expiration_date")
    fun getAll(): List<Product>

    /**
     * Retrieves all products from the database with the given uid's in the parameters.
     *
     * @param uids unique id's of products.
     * @return list of products as a result from the query.
     */
    @Query("SELECT * FROM product WHERE uid IN (:uids)")
    fun loadAllByIds(uids: IntArray): List<Product>

    /**
     * Retrieve single product from the database with the given product name in
     * the parameters
     *
     * @param name name of the product
     * @return single product as a result from the query.
     */
    @Query("SELECT * FROM product WHERE product_name LIKE :name")
    fun findByName(name: String): Product

    /**
     * Insert the given product from the parameters into the database.
     *
     * @param product product to be inserted.
     * @return the newly generated uid used for checking if the insertion has succeeded.
     */
    @Insert
    fun insert(product: Product): Long

    /**
     * Delete the given product from the database.
     *
     * @param product product to be deleted.
     */
    @Delete
    fun delete(product: Product)

}