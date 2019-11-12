package com.example.vittles.scanning

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.camera.core.CameraX
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.MeteringPoint
import androidx.navigation.fragment.NavHostFragment
import com.example.vittles.R
import com.example.vittles.scanning.ScannerPresenter.Companion.REQUEST_CODE_PERMISSIONS
import com.example.vittles.scanning.ScannerPresenter.Companion.REQUIRED_PERMISSIONS
import com.example.vittles.scanning.productaddmanual.DateEditView
import com.example.vittles.scanning.productaddmanual.ProductNameEditView
import com.example.vittles.services.scanner.DateFormatterService
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_camera.*
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * The scanner fragment that displays the camera preview with the checklist.
 *
 * @author Jeroen Flietstra
 */
class ScannerFragment @Inject internal constructor() : DaggerFragment(), ScannerContract.View {

    @Inject
    lateinit var presenter: ScannerPresenter

    private lateinit var textureView: TextureView
    private lateinit var refreshDate: ImageButton
    private lateinit var refreshProductName: ImageButton
    private lateinit var tvProductName: TextView
    private lateinit var tvExpirationDate: TextView
    private lateinit var ibEditName: ImageButton
    private lateinit var ibEditDate: ImageButton
    private lateinit var btnScanVittle: Button
    private lateinit var ivCheckboxBarcode: ImageView
    private lateinit var ivCheckboxExpirationDate: ImageView
    private lateinit var btnTorch: ImageButton

    private var expirationDate: DateTime? = null

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
        tvProductName = view.findViewById(R.id.tvProductName)
        tvExpirationDate = view.findViewById(R.id.tvExpirationDate)
        ibEditName = view.findViewById(R.id.ibEditName)
        ibEditDate = view.findViewById(R.id.ibEditDate)
        btnScanVittle = view.findViewById(R.id.btnScanVittle)
        ivCheckboxBarcode = view.findViewById(R.id.ivCheckboxBarcode)
        ivCheckboxExpirationDate = view.findViewById(R.id.ivCheckboxExpirationDate)
        btnTorch = view.findViewById(R.id.btnTorch)
        initViews()
        presenter.checkPermissions()
    }

    /**
     * Set up the tap to focus listener.
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun setUpTapToFocus() {
        textureView.setOnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_DOWN) {
                return@setOnTouchListener false
            }

            val factory = DisplayOrientedMeteringPointFactory(context!!, CameraX.LensFacing.BACK, textureView.width.toFloat(), textureView.height.toFloat())
            val point = factory.createPoint(event.x, event.y)
            val action = FocusMeteringAction.Builder.from(point).build()
            CameraX.getCameraControl(CameraX.LensFacing.BACK).startFocusAndMetering(action)
            return@setOnTouchListener true
        }
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

        btnTorch.setOnClickListener { onTorchButtonClicked() }

        setUpTapToFocus()
    }

    /**
     * Return the scanned barcode and expiration date.
     *
     */
    override fun onAddVittleButtonClick() {
        if (expirationDate != null && !tvProductName.text.isNullOrBlank()){
            val scanResult = ScanResult(tvProductName.text.toString(), expirationDate)
            NavHostFragment.findNavController(fragmentHost)
                .navigate(ScannerFragmentDirections.actionScannerFragmentToAddProductFragment(scanResult))
            CameraX.unbindAll()
        } else {
            Toast.makeText(
                context,
                "Scan or fill in the necessary fields",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Calls presenter to toggle torch and sets drawable of the torch status.
     *
     */
    override fun onTorchButtonClicked() {
        if (presenter.toggleTorch()) {
            btnTorch.setImageDrawable(
                context?.let {
                    getDrawable(
                        it,
                        R.drawable.ic_flash_on_black_36dp
                    )
                }
            )
        } else {
            btnTorch.setImageDrawable(
                context?.let {
                    getDrawable(
                        it,
                        R.drawable.ic_flash_off_black_36dp
                    )
                }
            )
        }
    }

    /**
     * Handles interface actions once the productName has been successfully scanned.
     *
     * @param productName The product name that has been retrieved from the camera.
     */
    override fun onBarcodeScanned(productName: String) {
        if (productName.isNotEmpty()) {
            tvProductName.text = productName
            ivCheckboxBarcode.setImageDrawable(
                context?.let {
                    getDrawable(
                        it,
                        R.drawable.ic_circle_darkened_filled
                    )
                }
            )
        }
        PreviewAnalyzer.hasBarCode = true
    }

    /**
     * Handles interface actions once the expirationDate has been successfully scanned
     *
     * @param text The text that has been retrieved from the camera
     */
    override fun onTextScanned(text: String) {
        tvExpirationDate.text = DateFormatterService.numberFormat.print(
            DateFormatterService.expirationDateFormatter(text)
        )
        expirationDate = DateFormatterService.expirationDateFormatter(text)!!
        ivCheckboxExpirationDate.setImageDrawable(
            context?.let {
                getDrawable(
                    it,
                    R.drawable.ic_circle_darkened_filled
                )
            }
        )
        PreviewAnalyzer.hasExpirationDate = true
    }

    /**
     * When error occurs with barcode show toast with error message.
     *
     */
    override fun onBarcodeNotFound() {
        Toast.makeText(
            context,
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
            context,
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
            context,
            "Permissions not granted by the user.",
            Toast.LENGTH_SHORT
        ).show()
        requireActivity().finish()
    }

    override fun onEditNameButtonClick() {
        val dialog = ProductNameEditView()
        context?.let { dialog.openDialog(it, tvProductName) }
    }

    override fun onEditExpirationButtonClick() {
        val dialog = DateEditView()
        context?.let { dialog.openDialog(it, tvExpirationDate) }
        requireActivity().onBackPressed()
    }
}
