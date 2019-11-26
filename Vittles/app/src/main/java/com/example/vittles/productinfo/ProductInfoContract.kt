package com.example.vittles.productinfo

import com.example.domain.product.Product
import com.example.vittles.enums.DeleteType

interface ProductInfoContract {

    interface View{
        fun initViews()
        fun updateViews()
        fun onNameChanged()
        fun onExpirationDateChanged()
        fun onProductUpdateSuccess()
        fun onProductUpdateFail()
    }

    interface Presenter{
        fun updateProduct(product: Product)
        fun deleteProduct(product: Product, deleteType: DeleteType)
    }
}