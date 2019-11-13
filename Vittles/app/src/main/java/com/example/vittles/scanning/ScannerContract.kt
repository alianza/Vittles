package com.example.vittles.scanning

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import com.example.domain.product.Product

/**
 * MVP Contract for scanning products.
 *
 * @author Jeroen Flietstra
 */
interface ScannerContract {

    interface View {
        fun initViews(view: android.view.View)
        fun initListeners()
        fun setUpTapToFocus()
        fun onAddVittleButtonClick()
        fun onBarcodeScanned(productName: String)
        fun onBarcodeNotFound()
        fun onTextScanned(text: String)
        fun onTextNotFound()
        fun onNoPermissionGranted()
        fun onEditNameButtonClick()
        fun onEditExpirationButtonClick()
        fun onTorchButtonClicked()
        fun onResetView()
        fun onResetDate()
        fun onResetProductName()
        fun onShowAddProductError()
        fun onShowAddProductSucceed()
        fun onShowCloseToExpirationPopup(product: Product)
        fun onProductNameEdited(productName: String)
        fun onExpirationDateEdited(text: String)
        fun onProductNameCheckboxChecked(productName: String)
        fun onExpirationDateCheckboxChecked(text: String)
    }

    interface Presenter {
        fun startCamera()
        fun getPreview(): Preview
        fun getImageAnalysis(): ImageAnalysis
        fun checkPermissions()
        fun allPermissionsGranted(): Boolean
    }
}