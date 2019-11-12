package com.example.data.retrofit

import com.example.data.retrofit.tesco.TescoResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Retrofit service that connects to the endpoints of the API.
 *
 * @author Jeroen Flietstra
 */
interface ProductsApiService {

    /**
     * Connects to the endpoint that retrieves product data based on a GTIN code.
     *
     * @param barcode The GTIN code of the product.
     * @return A [TescoResult] object with the retrieved data.
     */
    @Headers("Accept: application/json")
    @GET("product/")
    fun getProductName(@Query(value = "gtin", encoded = true) barcode: String): Observable<TescoResult>
}