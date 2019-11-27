package com.example.data.room.barcodedictionary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BarcodeDictionaryEntity (
    @PrimaryKey @ColumnInfo(name ="gtin") val barcode: Long,
    @ColumnInfo(name = "product_name") val productName: String?
)