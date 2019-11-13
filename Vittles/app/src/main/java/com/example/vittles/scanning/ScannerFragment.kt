package com.example.vittles.scanning

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.camera.core.CameraX
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.domain.product.Product
import com.example.vittles.NavigationGraphDirections
import com.example.vittles.R
import com.example.vittles.scanning.ScannerPresenter.Companion.REQUEST_CODE_PERMISSIONS
import com.example.vittles.scanning.ScannerPresenter.Companion.REQUIRED_PERMISSIONS
import com.example.vittles.scanning.productaddmanual.ProductNameEditView
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.example.vittles.services.scanner.DateFormatterService
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.selects.SelectClause1
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
    private lateinit var scanningPlane: ImageView

    private lateinit var vibrator: Vibrator

    private var expirationDate: DateTime? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@ScannerFragment)
        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
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

            val factory = DisplayOrientedMeteringPointFactory(
                context!!,
                CameraX.LensFacing.BACK,
                textureView.width.toFloat(),
                textureView.height.toFloat()
            )
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
    override fun initViews(view: View) {
        textureView = view.findViewById(R.id.textureView)
        refreshDate = view.findViewById(R.id.ibRefreshDate)
        refreshProductName = view.findViewById(R.id.ibRefreshProductName)
        tvProductName = view.findViewById(R.id.tvProductName)
        tvExpirationDate = view.findViewById(R.id.tvExpirationDate)
        ibEditName = view.findViewById(R.id.ibEditName)
        ibEditDate = view.findViewById(R.id.ibEditDate)
        btnScanVittle = view.findViewById(R.id.btnScanVittle)
        ivCheckboxBarcode = view.findViewById(R.id.ivCheckboxBarcode)
        ivCheckboxExpirationDate = view.findViewById(R.id.ivCheckboxExpirationDate)
        btnTorch = view.findViewById(R.id.btnTorch)
        scanningPlane = view.findViewById(R.id.scanningPlane)

        refreshDate.visibility = View.INVISIBLE
        refreshProductName.visibility = View.INVISIBLE

        setUpTapToFocus()
    }

    /**
     * Initializes the on click listeners.
     *
     */
    override fun initListeners() {
        btnScanVittle.setOnClickListener { onAddVittleButtonClick() }

        ibEditName.setOnClickListener { onEditNameButtonClick() }

        ibEditDate.setOnClickListener { onEditExpirationButtonClick() }

        btnTorch.setOnClickListener { onTorchButtonClicked() }

        refreshProductName.setOnClickListener { onResetProductName() }

        refreshDate.setOnClickListener { onResetDate() }
    }

    /**
     * Return the scanned barcode and expiration date.
     *
     */
    override fun onAddVittleButtonClick() {
        if (expirationDate != null && !tvProductName.text.isNullOrBlank()) {
            val product = Product(
                null,
                tvProductName.text.toString(),
                expirationDate!!,
                DateTime(),
                null
            )
            presenter.addProduct(product, true)
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
        onProductNameCheckboxChecked(productName)
        PreviewAnalyzer.hasBarCode = true
        refreshProductName.visibility = View.VISIBLE
        onScanSuccessful()
    }

    /**
     * Handles interface actions once the expirationDate has been successfully scanned
     *
     * @param text The text that has been retrieved from the camera
     */
    override fun onTextScanned(text: String) {
        onExpirationDateCheckboxChecked(text)
        PreviewAnalyzer.hasExpirationDate = true
        refreshDate.visibility = View.VISIBLE
        onScanSuccessful()
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
     * Lets the phone vibrate and colors the scanning plane.
     *
     */
    // Deprecation suppressed because we use an old API version
    @Suppress("DEPRECATION")
    fun onScanSuccessful() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(50)
        }
        ImageViewCompat.setImageTintList(scanningPlane, context?.let {
            ContextCompat.getColor(
                it, R.color.colorPrimary)
        }?.let { ColorStateList.valueOf(it) })
        Handler().postDelayed({
            ImageViewCompat.setImageTintList(scanningPlane, context?.let {
                ContextCompat.getColor(
                    it, R.color.black)
            }?.let { ColorStateList.valueOf(it) })
        }, 500)
    }

    /**
     * Puts the necessary values on the right place after edit.
     *
     * @param productName The new product name.
     */
    override fun onProductNameEdited(productName: String) {
        onProductNameCheckboxChecked(productName)
        PreviewAnalyzer.hasBarCode = true
        refreshProductName.visibility = View.VISIBLE
    }

    /**
     * Puts the necessary values on the right place after edit.
     *
     * @param text The new expiration date.
     */
    override fun onExpirationDateEdited(text: String) {
        onExpirationDateCheckboxChecked(text)
        PreviewAnalyzer.hasExpirationDate = true
        refreshDate.visibility = View.VISIBLE
    }

    /**
     * Checks the checkbox and fills in the text view.
     *
     * @param productName The new product name.
     */
    override fun onProductNameCheckboxChecked(productName: String) {
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
    }

    /**
     * Checks the checkbox and fills in the text view.
     *
     * @param text The new expiration date.
     */
    override fun onExpirationDateCheckboxChecked(text: String) {
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
    }

    /**
     * Resets the necessary date properties.
     *
     */
    override fun onResetDate(){
        tvExpirationDate.text = getString(R.string.date_format_scanner)
        ivCheckboxExpirationDate.setImageDrawable(
            context?.let {
                getDrawable(
                    it,
                    R.drawable.ic_circle_darkened
                )
            }
        )
        PreviewAnalyzer.hasExpirationDate = false
        refreshDate.visibility = View.INVISIBLE
    }

    /**
     * Resets the necessary product name properties.
     *
     */
    override fun onResetProductName() {
        tvProductName.text = getString(R.string.product_name_scanner)
        ivCheckboxBarcode.setImageDrawable(
            context?.let {
                getDrawable(
                    it,
                    R.drawable.ic_circle_darkened
                )
            }
        )
        PreviewAnalyzer.hasBarCode = false
        refreshProductName.visibility = View.INVISIBLE
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
     * Asks for the needed permissions, called if the user did not grant any permissions.
     *
     */
    fun onRequestPermissionsFromFragment() {
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    /**
     * If no permissions granted, show toast with error message and redirect to dashboard.
     *
     */
    override fun onNoPermissionGranted() {
        Toast.makeText(
            context,
            "Permissions not granted by the user.",
            Toast.LENGTH_SHORT
        ).show()
        NavHostFragment.findNavController(fragmentHost)
            .navigate(NavigationGraphDirections.actionGlobalProductListFragment())
    }

    /**
     * Opens the dialog to edit the product name.
     *
     */
    override fun onEditNameButtonClick() {
        val dialog = ProductNameEditView(onFinished = { productName: String ->
            onProductNameEdited(productName)
        })
        context?.let { dialog.openDialog(it, tvProductName.text.toString()) }
    }

    /**
     * Opens the date picker dialog to edit the expiration date.
     *
     */
    override fun onEditExpirationButtonClick() {
        val currentDate = DateTime.now()

        val year = currentDate.year
        val month = currentDate.monthOfYear
        val day = currentDate.dayOfMonth
        val dpd = context?.let { it1 ->
            DatePickerDialog(
                it1,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val expirationDate =
                        DateTime(year, monthOfYear + MONTHS_OFFSET, dayOfMonth, 0, 0)
                    val expDateText = getString(
                        R.string.expiration_format,
                        (expirationDate.dayOfMonth).toString(),
                        (expirationDate.monthOfYear).toString(),
                        (expirationDate.year).toString()
                    )
                    onExpirationDateEdited(expDateText)
                }, year, month - MONTHS_OFFSET, day
            )
        }
        if (dpd != null) {
            dpd.setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                getString(R.string.btn_cancel),
                dpd
            )
            dpd.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                getString(R.string.btn_confirm),
                dpd
            )
            dpd.datePicker.minDate = currentDate.millis
            dpd.show()
        }
    }

    /**
     * If product has been added, this method will reset all the necessary properties.
     *
     */
    override fun onResetView() {
        onResetDate()
        onResetProductName()
    }

    /**
     * If product could not be added, this method will create a feedback snack bar for the error.
     *
     */
    override fun onShowAddProductError() {
        Snackbar.make(layout, getString(R.string.product_name_invalid), Snackbar.LENGTH_LONG)
            .show()
    }

    /**
     * If product is added successfully, this method will show a toast displaying a success state.
     *
     */
    override fun onShowAddProductSucceed() {
        Snackbar.make(layout, getString(R.string.product_added), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the CloseToExpiring popup.
     *
     */
    override fun onShowCloseToExpirationPopup(product: Product) {
        context?.let {
            PopupManager.instance.showPopup(
                it,
                PopupBase(
                    "Almost expired!",
                    String.format(
                        "The scanned product expires in %d days. \n Are you sure you want to add it?",
                        product.getDaysRemaining()
                    )
                ),
                PopupButton("NO"),
                PopupButton("YES") { presenter.addProduct(product, false) }
            )
        }
    }

    companion object {
        /**
         * This offset is used to counter the default values from the Date object.
         *
         */
        const val MONTHS_OFFSET = 1
    }
}
