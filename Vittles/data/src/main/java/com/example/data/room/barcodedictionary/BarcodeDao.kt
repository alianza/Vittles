package com.example.data.room.barcodedictionary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Interface dao for some CRUD actions on the barcode data model.
 *
 * @author Jeroen Flietstra
 */
@Dao
interface BarcodeDao {

    /**
     * Retrieves entity by searching the barcode.
     *
     * @param barcode The barcode query to search.
     */
    @Query("SELECT * FROM BarcodeDictionaryEntity WHERE gtin LIKE :barcode")
    fun getBarcode(barcode: String): Maybe<BarcodeDictionaryEntity>

    /**
     * Insert entity into local database.
     *
     * @param barcodeDictionaryEntity The entity to insert.
     * @return Long value that returns the barcode if succeeded, otherwise it returns a negative value.
     */
    @Insert
    fun insertBarcode(barcodeDictionaryEntity: BarcodeDictionaryEntity)

    /**
     * Update entity in the local database.
     *
     * @param barcodeDictionaryEntity The entity to update.
     * @return Long value that returns the barcode if succeeded, otherwise it returns a negative value.
     */
    @Update
    fun updateBarcode(barcodeDictionaryEntity: BarcodeDictionaryEntity)
}