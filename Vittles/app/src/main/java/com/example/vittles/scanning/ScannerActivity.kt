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
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_camera.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
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

    private val regex = Regex("^([0-9&/:.-]*)\$")

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

        resetProductName.visibility = View.INVISIBLE
        resetExperationDate.visibility = View.INVISIBLE

        btnScanVittle.setOnClickListener { onAddVittleButtonClick() }
    }

    /**
     * Return the scanned barcode and expiration date.
     *
     */
    override fun onAddVittleButtonClick() {
        if (tvBarcode.text.toString().isNotBlank()) {
            val scanResult = ScanResult(tvBarcode.text.toString(), expirationDate)
            val resultIntent = Intent()
            resultIntent.putExtra(SCAN_RESULT, scanResult)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
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
            resetProductName.visibility = View.VISIBLE
        }
    }

    /**
     * Handles interface actions once the expirationDate has been successfully scanned
     *
     * @param text The text that has been retrieved from the camera
     */
    override fun onTextScanned(text: String) {
        val numberFormat = DateTimeFormat.forPattern("dd/MM/yyyy")
        val charFormat = DateTimeFormat.forPattern("dd/MMM/yyyy")

        val formatter = if (text.matches(regex)) {
            numberFormat
        } else {
            charFormat
        }

//      Replace all dividers to slashes for yodaTime
        var replacedText = text.replace('-', '/').replace(':', '/').replace('.', '/').replace(' ', '/')

//      Replace dutch abbreviated month names with english equivalents
        replacedText = replacedText.replace("okt", "oct").replace("mei", "may").replace("mrt", "mar")

//        var date = LocalDate.parse(replacedText, formatter)

        expirationDate = formatter.parseDateTime(replacedText)

        println(formatter.print(expirationDate))

        tvExpirationDate.text = numberFormat.print(expirationDate)
        ivCheckboxExpirationDate.setImageDrawable(getDrawable(R.drawable.ic_circle_darkened_filled))

        //Original thread error
        resetExperationDate.visibility = View.VISIBLE
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
