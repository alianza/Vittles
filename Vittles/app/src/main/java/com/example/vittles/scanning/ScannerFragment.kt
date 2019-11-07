package com.example.vittles.scanning

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.camera.core.CameraX
import com.example.vittles.R
import com.example.vittles.scanning.ScannerPresenter.Companion.REQUEST_CODE_PERMISSIONS
import com.example.vittles.scanning.ScannerPresenter.Companion.REQUIRED_PERMISSIONS
import com.example.vittles.scanning.productaddmanual.DateEditView
import com.example.vittles.scanning.productaddmanual.ProductNameEditView
import com.example.vittles.services.scanner.DateFormatterService
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import dagger.android.support.DaggerFragment
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime
import javax.inject.Inject

const val SCAN_RESULT = "SCAN_RESULT"

class ScannerFragment @Inject internal constructor(): DaggerFragment(), ScannerContract.View {

    @Parcelize
    data class ScanResult (
        val productName: String?,
        val expirationDate: DateTime?
    ) : Parcelable

    @Inject
    lateinit var presenter: ScannerPresenter

    private lateinit var textureView: TextureView
    private lateinit var refreshDate: ImageButton
    private lateinit var refreshProductName: ImageButton
    private lateinit var tvBarcode: TextView
    private lateinit var tvExpirationDate: TextView
    private lateinit var ibEditName: ImageButton
    private lateinit var ibEditDate: ImageButton
    private lateinit var btnScanVittle: Button
    private lateinit var ivCheckboxBarcode: ImageView
    private lateinit var ivCheckboxExpirationDate: ImageView

    private lateinit var expirationDate: DateTime

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@ScannerFragment)
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textureView = view.findViewById(R.id.textureView)
        refreshDate = view.findViewById(R.id.refreshDate)
        refreshProductName = view.findViewById(R.id.refreshProductName)
        tvBarcode = view.findViewById(R.id.tvBarcode)
        tvExpirationDate = view.findViewById(R.id.tvExpirationDate)
        ibEditName = view.findViewById(R.id.ibEditName)
        ibEditDate = view.findViewById(R.id.ibEditDate)
        btnScanVittle = view.findViewById(R.id.btnScanVittle)
        ivCheckboxBarcode = view.findViewById(R.id.ivCheckboxBarcode)
        ivCheckboxExpirationDate = view.findViewById(R.id.ivCheckboxExpirationDate)
        initViews()
        presenter.checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraX.unbindAll()
        presenter.destroy()
    }

    /**
     * Initializes view elements.
     *
     */
    override fun initViews() {
        refreshDate.visibility = View.INVISIBLE
        refreshProductName.visibility = View.INVISIBLE

        btnScanVittle.setOnClickListener { onAddVittleButtonClick() }

        ibEditName.setOnClickListener { onEditNameButtonClick() }

        ibEditDate.setOnClickListener { onEditExpirationButtonClick() }
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
//            setResult(Activity.RESULT_OK, resultIntent)
            CameraX.unbindAll()
        } else {
            CameraX.unbindAll()
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
            ivCheckboxBarcode.setImageDrawable(getDrawable(context!!, R.drawable.ic_circle_darkened_filled))
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
        ivCheckboxExpirationDate.setImageDrawable(getDrawable(context!!, R.drawable.ic_circle_darkened_filled))
        PreviewAnalyzer.hasExpirationDate = true
    }

    /**
     * When error occurs with barcode show toast with error message.
     *
     */
    override fun onBarcodeNotFound() {
            Toast.makeText(
                context!!,
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
            context!!,
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

    fun onRequestPermissionsFromFragment() {
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    /**
     * If no permissions granted, show toast with error message.
     *
     */
    override fun onNoPermissionGranted() {
        Toast.makeText(
            context!!,
            "Permissions not granted by the user.",
            Toast.LENGTH_SHORT
        ).show()
        requireActivity().finish()
    }

    override fun onEditNameButtonClick() {
        val dialog = ProductNameEditView()
        dialog.openDialog(context!!, tvBarcode)
    }

    override fun onEditExpirationButtonClick() {
        val dialog = DateEditView()
        dialog.openDialog(context!!, tvExpirationDate)
    }
}
