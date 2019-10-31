package com.example.domain.barcode

import com.example.domain.repositories.ProductsApi
import javax.inject.Inject

class GetProductByBarcode @Inject constructor(private val productsApi: ProductsApi) {
}