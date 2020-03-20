package com.example.vittles.dashboard.productinfo

import com.example.domain.barcode.ProductDictionary
import com.example.vittles.dashboard.model.ProductViewModel
import org.joda.time.DateTime

/**
 * @author Arjen Simons
 */
interface ProductInfoContract {

    interface View{
        fun initViews()
        fun setListeners()
        fun updateViews()
        fun onEditNameClicked()
        fun onEditExpirationDateClicked()
        fun onEatenButtonClicked()
        fun onDeleteButtonClicked()
        fun onNameChanged(productName: String)
        fun onExpirationDateChanged(expirationDate: DateTime)
        fun onProductUpdateSuccess()
        fun onProductUpdateFail()
    }

    interface Presenter{
        fun onProductUpdate(product: ProductViewModel)
        fun patchProductDictionary(productDictionary: ProductDictionary)
    }
}