package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

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
    @Query("SELECT * FROM ProductEntity ORDER BY expiration_date")
    fun getAll(): Single<List<ProductEntity>>

    /**
     * Retrieves all products from the database with the given uid's in the parameters.
     *
     * @param uids unique id's of products.
     * @return list of products as a result from the query.
     */
    @Query("SELECT * FROM ProductEntity WHERE uid IN (:uids)")
    fun loadAllByIds(uids: IntArray): List<ProductEntity>

    /**
     * Retrieve single product from the database with the given product name in
     * the parameters
     *
     * @param name name of the product
     * @return single product as a result from the query.
     */
    @Query("SELECT * FROM ProductEntity WHERE product_name LIKE :name")
    fun findByName(name: String): ProductEntity

    /**
     * Insert the given product from the parameters into the database.
     *
     * @param product product to be inserted.
     * @return the newly generated uid used for checking if the insertion has succeeded.
     */
    @Insert
    fun insert(product: ProductEntity): Long

    /**
     * Delete the given product from the database.
     *
     * @param product product to be deleted.
     */
    @Delete
    fun delete(product: ProductEntity): Int


    @Insert
    fun insertWasteReportProduct(wasteReportProduct: WasteReportEntity)


    @Query("SELECT COUNT(waste_type) FROM WasteReportEntity WHERE waste_type = 'EATEN' AND creation_date >= :date")
    fun getCountEatenProducts(date: Long): Single<Int>

    @Query("SELECT COUNT(waste_type) FROM WasteReportEntity WHERE waste_type = 'THROWN_AWAY' AND creation_date >= :date")
    fun getCountExpiredProducts(date: Long): Single<Int>

    @Query("SELECT Cast(tot1 AS FLOAT)/(CAST(tot2 AS FLOAT)+CAST(tot1 AS FLOAT))*100.00 FROM (SELECT COUNT(waste_type) AS tot1 FROM WasteReportEntity WHERE waste_type = 'EATEN' AND creation_date >= :date) as float ,(SELECT COUNT(waste_type)AS tot2 FROM WasteReportEntity WHERE waste_type = 'THROWN_AWAY' AND creation_date >= :date) as float")
    fun getWastePercent(date: Long): Single<Int>
}