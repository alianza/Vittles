package com.example.vittles.productlist

import com.example.domain.model.Product

/**
 * MVP Contract for displaying list of notes.
 */
interface ProductsContract {

    interface View {
        fun onProductsLoad(products: List<Product>)
        fun onProductsLoadFail()
    }

    interface Presenter {
        fun loadProducts()
    }
}