package com.example.vittles.scanning

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.TextureView
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraX
import com.example.vittles.R
import com.example.vittles.scanning.productaddmanual.DateEditView
import com.example.vittles.scanning.productaddmanual.ProductNameEditView
import com.example.vittles.services.scanner.DateFormatterService
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_camera.*
import org.joda.time.DateTime
import javax.inject.Inject

const val SCAN_RESULT = "SCAN_RESULT"

class ScannerActivity @Inject internal constructor(): DaggerAppCompatActivity(), ScannerContract.View {

    @Parcelize
    data class ScanResult (
        val productName: String?,
        val expirationDate: DateTime?
    ) : Parcelable

    @Inject
    lateinit var presenter: ScannerPresenter

    private lateinit var textureView: TextureView

    private lateinit var expirationDate: DateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start(this@ScannerActivity)
        setContentView(R.layout.activity_camera)
        initViews()
        presenter.checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**
     * Initializes view elements.
     *
     */
    override fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.scan_vittles_title)

        textureView = findViewById(R.id.textureView)

        refreshDate.visibility = View.INVISIBLE
        refreshProductName.visibility = View.INVISIBLE

        btnScanVittle.setOnClickListener { onAddVittleButtonClick() }

        ibEditName.setOnClickListener { onEditNameButtonClick() }

        ibEditDate.setOnClickListener { onEditExperationButtonClick() }
    }

    /**
     * Return the scanned barcode and expiration date.
     *
     */
    override fun onAddVittleButtonClick() {
        if (tvBarcode.text.toString().isNotBlank() && tvBarcode.text.toString() != "PRODUCT NAME") {
            val scanResult = ScanResult(tvBarcode.text.toString(), expirationDate)
            val resultIntent = Intent()
            resultIntent.putExtra(SCAN_RESULT, scanResult)
            setResult(Activity.RESULT_OK, resultIntent)
            CameraX.unbindAll()
            finish()
        } else {
            CameraX.unbindAll()
            finish()
        }

    }

    /**
     * Handles interface actions once the barcode has been successfully scanned.
     *
     * @param barcodes The barcodes that have been retrieved from the camera.
     */
    override fun onBarcodeScanned(barcodes: List<FirebaseVisionBarcode>) {
        if (barcodes.isNotEmpty()) {
            tvBarcode.text = barcodes[0].rawValue
            ivCheckboxBarcode.setImageDrawable(getDrawable(R.drawable.ic_circle_darkened_filled))
        }
        PreviewAnalyzer.hasBarCode = true
    }

    /**
     * Handles interface actions once the expirationDate has been successfully scanned
     *
     * @param text The text that has been retrieved from the camera
     */
    override fun onTextScanned(text: String) {
        tvExpirationDate.text = DateFormatterService.numberFormat.print(DateFormatterService.expirationDateFormatter(text))
        expirationDate = DateFormatterService.expirationDateFormatter(text)!!
        ivCheckboxExpirationDate.setImageDrawable(getDrawable(R.drawable.ic_circle_darkened_filled))
        PreviewAnalyzer.hasExpirationDate = true
    }

    /**
     * When error occurs with barcode show toast with error message.
     *
     */
    override fun onBarcodeNotFound() {
            Toast.makeText(
                this,
                "Something went wrong!",
                Toast.LENGTH_SHORT
            ).show()
    }

    /**
     * When error occurs with text recognition show toast with error message.
     *
     */
    override fun onTextNotFound() {
        Toast.makeText(
            this,
            "Something went wrong! TEXt",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (presenter.allPermissionsGranted()) {
                textureView.post { presenter.startCamera() }
            } else {
                onNoPermissionGranted()
            }
        }
    }

    /**
     * If no permissions granted, show toast with error message.
     *
     */
    override fun onNoPermissionGranted() {
        Toast.makeText(
            this,
            "Permissions not granted by the user.",
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun onEditNameButtonClick() {
        val dialog = ProductNameEditView()
        dialog.openDialog(this, tvBarcode)
    }

    fun onEditExperationButtonClick() {
        val dialog = DateEditView()
        dialog.openDialog(this, tvExpirationDate)
    }

    /**
     * Listener of the back button.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return onBackButtonClick()
    }

    /**
     * Terminates the scan product activity.
     *
     * @return boolean that represents if action succeeded
     */
    override fun onBackButtonClick(): Boolean {
        return try {
            CameraX.unbindAll()
            finish()
            true
        } catch (exception: Exception) {
            false
        }
    }
}
