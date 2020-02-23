package com.example.data.room.product

import androidx.room.*
import com.example.data.room.wastereport.WasteReportEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface ProductDao {

    @Query("SELECT * FROM ProductEntity ORDER BY expiration_date")
    fun getAll(): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE LOWER(product_name) LIKE :query ORDER BY expiration_date ASC")
    fun getAllByExpirationDateAscWithQuery(query: String): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE LOWER(product_name) LIKE :query ORDER BY expiration_date DESC")
    fun getAllByExpirationDateDescWithQuery(query: String): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE LOWER(product_name) LIKE :query ORDER BY product_name ASC")
    fun getAllByProductNameAscWithQuery(query: String): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE LOWER(product_name) LIKE :query ORDER BY product_name DESC")
    fun getAllByProductNameDescWithQuery(query: String): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE LOWER(product_name) LIKE :query ORDER BY creation_date ASC")
    fun getAllByCreationDateAscWithQuery(query: String): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE LOWER(product_name) LIKE :query ORDER BY creation_date DESC")
    fun getAllByCreationDateDescWithQuery(query: String): Observable<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE uid IN (:uids)")
    fun loadAllByIds(uids: IntArray): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity WHERE product_name LIKE :name")
    fun findByName(name: String): ProductEntity

    @Insert
    fun insert(product: ProductEntity): Long

    @Update
    fun update(product: ProductEntity): Int

    @Delete
    fun delete(product: ProductEntity): Int

    @Insert
    fun insertWasteReportProduct(wasteReportProduct: WasteReportEntity)

    @Query("SELECT COUNT(waste_type) FROM WasteReportEntity WHERE waste_type = 'EATEN' AND creation_date >= :date")
    fun getCountEatenProducts(date: Long): Single<Int>

    @Query("SELECT COUNT(waste_type) FROM WasteReportEntity WHERE waste_type = 'THROWN_AWAY' AND creation_date >= :date")
    fun getCountExpiredProducts(date: Long): Single<Int>

    @Query("SELECT * FROM WasteReportEntity WHERE creation_date >= :date")
    fun getWasteReportProducts(date: Long): Single<List<WasteReportEntity>>
}