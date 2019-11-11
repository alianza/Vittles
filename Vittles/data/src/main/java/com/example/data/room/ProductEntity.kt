package com.example.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

/**
 * ProductEntity data model.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Arjen Simons
 *
 * @property uid unique id used as surrogate key.
 * @property productName name of the product.
 * @property expirationDate date of expiration.
 * @property creationDate date of when product was added to database.
 */
@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "expiration_date") val expirationDate: DateTime,
    @ColumnInfo(name = "creation_date") val creationDate: DateTime
)
