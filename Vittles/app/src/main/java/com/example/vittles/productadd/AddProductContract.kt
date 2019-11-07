package com.example.vittles.productadd

import com.example.domain.product.Product
import com.example.vittles.scanning.ScanResult

/**
 * MVP Contract for adding products.
 *
 * @author Jeroen Flietstra
 */
interface AddProductContract {

    interface View {
        fun initViews()
        fun initDatePicker()
        fun onConfirmButtonClick()
        fun onScanButtonClick()
        fun onScanResult(scanResult: ScanResult)
        fun onResetView()
        fun onShowAddProductError()
        fun onShowAddProductSucceed()
        fun onShowCloseToExpirationPopup(product: Product)
        fun validate(): Boolean
    }

    interface Presenter {
        fun addProduct(product: Product, checkDate: Boolean = true)
    }
}