package com.example.vittles.productadd

import com.example.domain.product.Product

/**
 * MVP Contract for adding products.
 *
 * @author Jeroen Flietstra
 */
interface AddProductContract {

    interface View {
        fun initViews()
        fun initDatePicker()
        // TODO implement backbutton
//        fun onBackButtonClick() : Boolean
        fun onConfirmButtonClick()
        fun validate(): Boolean
        fun resetView()
        fun showAddProductError()
        fun showAddProductSucceed()
        fun showCloseToExpirationPopup(product: Product)
    }

    interface Presenter {
        fun addProduct(product: Product, checkDate: Boolean = true)
    }
}