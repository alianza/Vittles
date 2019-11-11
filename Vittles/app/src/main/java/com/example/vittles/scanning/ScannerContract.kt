package com.example.vittles.scanning

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.text.FirebaseVisionText

/**
 * MVP Contract for scanning products.
 *
 * @author Jeroen Flietstra
 */
interface ScannerContract {

    interface View {
        fun initViews()
        fun onAddVittleButtonClick()
        fun onBarcodeScanned(barcodes: List<FirebaseVisionBarcode>)
        fun onBarcodeNotFound()
        fun onTextScanned(text: String)
        fun onTextNotFound()
        fun onNoPermissionGranted()
        fun onEditNameButtonClick()
        fun onEditExpirationButtonClick()
    }

    interface Presenter {
        fun startCamera()
        fun getPreview(): Preview
        fun getImageAnalysis(): ImageAnalysis
        fun checkPermissions()
        fun allPermissionsGranted(): Boolean
    }
}