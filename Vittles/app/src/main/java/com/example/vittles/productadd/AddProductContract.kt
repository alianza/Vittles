package com.example.vittles.productadd

import com.example.domain.model.Product

interface AddProductContract {

    interface View {
        fun onProductAdd()
    }

    interface Presenter {
        fun addProduct(product: Product)
    }
}