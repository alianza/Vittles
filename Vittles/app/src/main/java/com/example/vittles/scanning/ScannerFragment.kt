package com.example.vittles.scanning

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.*
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.camera.core.CameraX
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.ImageViewCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.domain.consts.DAYS_REMAINING_EXPIRED
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
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * The scanner fragment that displays the camera preview with the checklist.
 *
 * @author Jeroen Flietstra
 */
class ScannerFragment @Inject internal constructor() : DaggerFragment(), ScannerContract.View {

    /**
     * The presenter of the fragment
     */
    @Inject
    lateinit var presenter: ScannerPresenter

    /** The vibration manager used for vibration when a product is scanned. */
    private lateinit var vibrator: Vibrator

    /** @suppress */
    private var expirationDate: DateTime? = null


    /** {@inheritDoc} */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@ScannerFragment)
        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        presenter.checkPermissions()
    }

    /** {@inheritDoc} */
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
        ibRefreshDate.visibility = View.INVISIBLE
        ibRefreshProductName.visibility = View.INVISIBLE
    }

    /**
     * Initializes the on click listeners.
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun initListeners() {
        btnScanVittle.setOnClickListener { onAddVittleButtonClick() }

        ibEditName.setOnClickListener { onEditNameButtonClick() }

        ibEditDate.setOnClickListener { onEditExpirationButtonClick() }

        btnTorch.setOnClickListener { onTorchButtonClicked() }

        ibRefreshProductName.setOnClickListener { onResetProductName() }

        ibRefreshDate.setOnClickListener { onResetDate() }

        textureView.setOnTouchListener { _, event -> onTapToFocus(event) }

        btnUseCamera.setOnClickListener { presenter.checkPermissions() }
    }

    /**
     * Set up the tap to focus listener.
     *
     */
    override fun onTapToFocus(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) {
            return false
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
        return true
    }

    /**
     * Return the scanned barcode and expiration date.
     *
     */
    override fun onAddVittleButtonClick() {
        val product = Product(
            null,
            tvProductName.text.toString(),
            expirationDate!!,
            DateTime(),
            null
        )
        presenter.addProduct(product, true)
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
     * Toggle's the status of the add Vittle button based on retrieved properties
     *
     */
    private fun toggleAddVittleButton() {
        if (expirationDate != null && tvProductName.text != getString(R.string.product_name_scanner)) {
            enableAddVittleButton()
        } else {
            disableAddVittleButton()
        }
    }

    /**
     * Disables the add Vittle button
     *
     */
    private fun disableAddVittleButton() {
        btnScanVittle.isEnabled = false
        btnScanVittle.alpha = 0.5F
    }

    /**
     * Enables the add Vittle button
     *
     */
    private fun enableAddVittleButton() {
        btnScanVittle.isEnabled = true
        btnScanVittle.alpha = 1F
    }

    /**
     * Handles interface actions once the productName has been successfully scanned.
     *
     * @param productName The product name that has been retrieved from the camera.
     */
    override fun onBarcodeScanned(productName: String) {
        if (!PreviewAnalyzer.hasBarCode) {
            if (productName.isDigitsOnly()) {
                onShowEditNameDialog(true)
            }
            onProductNameCheckboxChecked(productName)
            ibRefreshProductName.visibility = View.VISIBLE
            PreviewAnalyzer.hasBarCode = true
            onScanSuccessful()
        }
    }

    /**
     * Handles interface actions once the expirationDate has been successfully scanned
     *
     * @param text The text that has been retrieved from the camera
     */
    override fun onTextScanned(text: String) {
        if (!PreviewAnalyzer.hasExpirationDate) {
            onExpirationDateCheckboxChecked(text)
            PreviewAnalyzer.hasExpirationDate = true
            ibRefreshDate.visibility = View.VISIBLE
            onScanSuccessful()
        }
    }

    /**
     * When error occurs with barcode show toast with error message.
     *
     */
    override fun onBarcodeNotFound() {
        Toast.makeText(
            context,
            context!!.getString(R.string.no_scanning),
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
            context!!.getString(R.string.no_scanning),
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
        // Vibrate
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(50)
        }
        // Turn scanning plane green, and back after 500 ms
        ImageViewCompat.setImageTintList(scanningPlane, context?.let {
            ContextCompat.getColor(
                it, R.color.colorPrimary
            )
        }?.let { ColorStateList.valueOf(it) })
        Handler().postDelayed({
            ImageViewCompat.setImageTintList(scanningPlane, context?.let {
                ContextCompat.getColor(
                    it, R.color.black
                )
            }?.let { ColorStateList.valueOf(it) })
        }, 500)
        toggleAddVittleButton()
    }

    /**
     * Puts the necessary values on the right place after edit.
     *
     * @param productName The new product name.
     */
    override fun onProductNameEdited(productName: String) {
        onProductNameCheckboxChecked(productName)
        PreviewAnalyzer.hasBarCode = true
        ibRefreshProductName.visibility = View.VISIBLE
        toggleAddVittleButton()
    }

    /**
     * Puts the necessary values on the right place after edit.
     *
     * @param text The new expiration date.
     */
    override fun onExpirationDateEdited(text: String) {
        onExpirationDateCheckboxChecked(text)
        PreviewAnalyzer.hasExpirationDate = true
        ibRefreshDate.visibility = View.VISIBLE
        toggleAddVittleButton()
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
    override fun onResetDate() {
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
        this.expirationDate = null
        ibRefreshDate.visibility = View.INVISIBLE
        toggleAddVittleButton()
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
        ibRefreshProductName.visibility = View.INVISIBLE
        toggleAddVittleButton()
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
                btnUseCamera.visibility = View.GONE
                btnTorch.visibility = View.VISIBLE
            }
            else {
                onNoPermissionGranted()
            }
        }
    }

    /**
     * Asks for the needed permissions, called if the user did not grant any permissions.
     *
     */
    override fun onRequestPermissionsFromFragment() {
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    /**
     * If no permissions granted, show toast with error message and redirect to dashboard.
     *
     */
    override fun onNoPermissionGranted() {
        btnUseCamera.visibility = View.VISIBLE
        btnTorch.visibility = View.GONE
    }

    /**
     * Opens the dialog to edit the product name.
     *
     */
    override fun onEditNameButtonClick() {
        onShowEditNameDialog()
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
     * Opens the edit product name dialog.
     *
     * @param showMessage Boolean value that represents if the message should be shown.
     */
    override fun onShowEditNameDialog(showMessage: Boolean) {
        val dialog = ProductNameEditView(onFinished = { productName: String ->
            onProductNameEdited(productName)
        }, showMessage = showMessage)
        context?.let { dialog.openDialog(it, tvProductName.text.toString()) }
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
     * Calls either the close to expiration date or already expired pop-ups
     *
     * @param product Product to decide of if CloseToExpirationPopup or AlreadyExpiredPopup should be shown
     */
    @SuppressLint("DefaultLocale")
    override fun onShowExpirationPopup(product: Product) {
        if (product.getDaysRemaining() > DAYS_REMAINING_EXPIRED) {
            onShowCloseToExpirationPopup(product)
        } else {
            onShowAlreadyExpiredPopup(product)
        }
    }

    /**
     * Shows the CloseToExpiring popup.
     *
     * @param product Product to show CloseToExpirationPopup of
     */
    @SuppressLint("DefaultLocale")
    override fun onShowCloseToExpirationPopup(product: Product) {
        val multipleDaysChar = if (product.getDaysRemaining() == 1) { "" } else { "s" }

        context?.let {
            PopupManager.instance.showPopup(
                it,
                PopupBase(
                    getString(R.string.close_to_expiration_header),
                    getString(R.string.close_to_expiration_subText,
                        product.getDaysRemaining(),
                        multipleDaysChar
                    )
                ),
                PopupButton(getString(R.string.btn_no).toUpperCase()),
                PopupButton(getString(R.string.btn_yes).toUpperCase()) {
                    presenter.addProduct(
                        product,
                        false
                    )
                }
            )
        }
    }

    /**
     * Shows the AlreadyExpired popup.
     *
     * @param product Product to show AlreadyExpiredPopup of
     */
    @SuppressLint("DefaultLocale")
    override fun onShowAlreadyExpiredPopup(product: Product) {

        context?.let {
            PopupManager.instance.showPopup(
                it,
                PopupBase(
                    getString(R.string.already_expired_header),
                    getString(R.string.already_expired_subText)
                ),
                PopupButton(getString(R.string.btn_no).toUpperCase()),
                PopupButton(getString(R.string.btn_yes).toUpperCase()) {
                    presenter.addProduct(
                        product,
                        false
                    )
                }
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
