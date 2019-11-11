package com.example.data.retrofit

import com.example.data.retrofit.tesco.TescoResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ProductsApiService {

    @Headers("Accept: application/json", "Ocp-Apim-Subscription-Key: bb12529b7dda4bb4b7a6f25d1c146891")
    @GET("product/")
    fun getProductName(@Query(value = "gtin", encoded = true) barcode: String): Observable<TescoResult>
}