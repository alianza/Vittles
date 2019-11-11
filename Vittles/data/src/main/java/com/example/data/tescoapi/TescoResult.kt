package com.example.data.tescoapi

import com.google.gson.annotations.SerializedName

data class TescoResult(
    @SerializedName("products") var products: Array<TescoProduct>?
)