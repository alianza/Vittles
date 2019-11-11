package com.example.data.retrofit.tesco

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class TescoResult(
    @SerializedName("products") var products: Array<TescoProduct>?
)