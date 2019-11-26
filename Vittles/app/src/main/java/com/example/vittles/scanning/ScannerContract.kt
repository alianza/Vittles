package com.example.vittles.scanning

import android.view.MotionEvent
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
        fun onTapToFocus(event: MotionEvent): Boolean
        fun onAddVittleButtonClick()
        fun onBarcodeScanned(productName: String)
        fun onBarcodeNotFound()
        fun onTextScanned(text: String)
        fun onTextNotFound()
        fun onNoPermissionGranted()
        fun onRequestPermissionsFromFragment()
        fun onEditNameButtonClick()
        fun onEditExpirationButtonClick()
        fun onTorchButtonClicked()
        fun onResetView()
        fun onResetDate()
        fun onResetProductName()
        fun onShowAddProductError()
        fun onShowAddProductSucceed()
        fun onShowExpirationPopup(product: Product)
        fun onShowCloseToExpirationPopup(product: Product)
        fun onShowAlreadyExpiredPopup(product: Product)
        fun onProductNameEdited(productName: String)
        fun onExpirationDateEdited(text: String)
        fun onProductNameCheckboxChecked(productName: String)
        fun onExpirationDateCheckboxChecked(text: String)
        fun onShowEditNameDialog(showMessage: Boolean = false)
    }

    interface Presenter {
        fun startCamera()
        fun getPreview(): Preview
        fun getImageAnalysis(): ImageAnalysis
        fun checkPermissions()
        fun allPermissionsGranted(): Boolean
    }
}