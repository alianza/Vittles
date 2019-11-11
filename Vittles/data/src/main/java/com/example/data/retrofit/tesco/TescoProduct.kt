package com.example.data.retrofit.tesco

import com.google.gson.annotations.SerializedName

data class TescoProduct(
    @SerializedName("gtin") var barcode: String,
    @SerializedName("description") var value: String
)