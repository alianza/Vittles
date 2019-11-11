package com.example.data

import android.telecom.Call
import retrofit2.http.GET

interface ProductsApiService {

    @GET
    fun getProductName(barcode: String): Call
}