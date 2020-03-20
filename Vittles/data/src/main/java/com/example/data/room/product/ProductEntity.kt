package com.example.data.room.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "expiration_date") val expirationDate: DateTime,
    @ColumnInfo(name = "creation_date") val creationDate: DateTime,
    @ColumnInfo(name = "barcode") val barcode: String?
)
