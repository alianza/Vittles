package com.example.vittles.scanning

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview

/**
 * MVP Contract for scanning products.
 *
 * @author Jeroen Flietstra
 */
interface ScannerContract {

    interface View {
        fun initViews()
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
    }

    interface Presenter {
        fun startCamera()
        fun getPreview(): Preview
        fun getImageAnalysis(): ImageAnalysis
        fun checkPermissions()
        fun allPermissionsGranted(): Boolean
    }
}