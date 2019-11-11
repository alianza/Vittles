package com.example.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created for future development
 *
 */
class ProductsApiImpl {
    companion object {
        // The base url of the API
        private const val baseUrl = "https://dev.tescolabs.com/"

        /**
         *
         *
         * @return [ProductsApiService] The service class off the retrofit client.
         */
        fun createApi(): ProductsApiService {
            // Create an OkHttpClient to be able to make a log of the network traffic
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            // Create the Retrofit instance
            val productsApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            // Return the Retrofit NumbersApiService
            return productsApi.create(ProductsApiService::class.java)
        }
    }
}