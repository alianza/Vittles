package com.example.vittles.scanning

import android.view.MotionEvent
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import com.example.domain.barcode.BarcodeDictionary
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
        fun onBarcodeScanned(barcodeDictionary: BarcodeDictionary)
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
        fun onProductNameEdited(barcodeDictionary: BarcodeDictionary, notFound: Boolean = false)
        fun onExpirationDateEdited(text: String)
        fun onProductNameCheckboxChecked(productName: String)
        fun onExpirationDateCheckboxChecked(text: String)
        fun onShowEditNameDialog(notFound: Boolean = false, barcodeDictionary: BarcodeDictionary)
    }

    interface Presenter {
        fun addProduct(product: Product, checkDate: Boolean)
        fun addBarcode(barcodeDictionary: BarcodeDictionary)
        fun updateBarcode(barcodeDictionary: BarcodeDictionary)
        fun startCamera()
        fun getPreview(): Preview
        fun getImageAnalysis(): ImageAnalysis
        fun checkPermissions()
        fun allPermissionsGranted(): Boolean
    }
}