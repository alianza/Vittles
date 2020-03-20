package com.example.vittles.scanning

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.camera.core.CameraX
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.barcode.ProductDictionary
import com.example.domain.consts.DAYS_REMAINING_EXPIRED
import com.example.domain.product.ProductDictionaryStatus
import com.example.domain.product.model.Product
import com.example.vittles.NavigationGraphDirections
import com.example.vittles.R
import com.example.vittles.app.VittlesApp.PermissionProperties.REQUEST_CODE_PERMISSIONS
import com.example.vittles.app.VittlesApp.PermissionProperties.REQUIRED_PERMISSIONS
import com.example.vittles.enums.PreviousFragmentIndex
import com.example.vittles.extension.createSnackbar
import com.example.vittles.scanning.productaddmanual.ProductNameEditView
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.example.vittles.services.scanner.DateFormatterService
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_scanner.*
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

/**
 * The scanner fragment that displays the camera preview with the checklist.
 *
 * @author Jeroen Flietstra
 */
class ScannerFragment @Inject internal constructor() : DaggerFragment(), ScannerContract.View {

    @Inject
    lateinit var presenter: ScannerPresenter

    private val args: ScannerFragmentArgs by navArgs()

    private lateinit var vibrator: Vibrator

    private var barcodeDictionary =
        ProductDictionary(
            ProductDictionaryStatus.NOT_READY() as String,
            ProductDictionaryStatus.NOT_READY() as String
        )

    private var expirationDate: DateTime? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@ScannerFragment)
        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        initViews(view)
        initListeners()
        presenter.checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraX.unbindAll()
        presenter.destroy()
    }

    override fun initViews(view: View) {
        ibRefreshDate.visibility = View.INVISIBLE
        ibRefreshProductName.visibility = View.INVISIBLE
        toggleAddVittleButton()
    }

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

    private fun initTorchState() {
        if (presenter.torchEnabled()) {
            btnTorch.setImageDrawable(
                context?.let {
                    getDrawable(
                        it,
                        R.drawable.ic_flash_on_black_28dp
                    )
                }
            )
        } else {
            btnTorch.setImageDrawable(
                context?.let {
                    getDrawable(
                        it,
                        R.drawable.ic_flash_off_black_28dp
                    )
                }
            )
        }
    }

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

    override fun onAddVittleButtonClick() {
        val product = Product(
            0,
            tvProductName.text.toString(),
            expirationDate!!,
            DateTime(),
            barcodeDictionary.barcode
        )
        presenter.addProductToList(product, true)
        if (!barcodeDictionary.containsNotReady() && !barcodeDictionary.containsNotFound()) {
            presenter.patchProductDictionary(barcodeDictionary)
        }
    }

    override fun onTorchButtonClicked() {
        presenter.toggleTorch()
        initTorchState()
    }

    private fun toggleAddVittleButton() {
        if (expirationDate != null && tvProductName.text != getString(R.string.product_name_scanner)) {
            enableAddVittleButton()
        } else {
            disableAddVittleButton()
        }
    }

    private fun disableAddVittleButton() {
        btnScanVittle.isEnabled = false
        btnScanVittle.alpha = 0.5F
    }

    private fun enableAddVittleButton() {
        btnScanVittle.isEnabled = true
        btnScanVittle.alpha = 1F
    }

    override fun onBarcodeScanned(productDictionary: ProductDictionary) {
        if (!PreviewAnalyzer.hasBarCode) {
            this.barcodeDictionary = productDictionary
            if (productDictionary.containsNotFound()) {
                onShowEditNameDialog(productDictionary)
                onProductNameCheckboxChecked(productDictionary.barcode)
            } else {
                productDictionary.productName?.let { onProductNameCheckboxChecked(it) }
            }
            ibRefreshProductName.visibility = View.VISIBLE
            PreviewAnalyzer.hasBarCode = true
            onScanSuccessful()
        }
    }

    override fun onTextScanned(text: String) {
        if (!PreviewAnalyzer.hasExpirationDate) {
            onExpirationDateCheckboxChecked(text)
            PreviewAnalyzer.hasExpirationDate = true
            ibRefreshDate.visibility = View.VISIBLE
            onScanSuccessful()
        }
    }

    override fun onBarcodeNotFound() {
        Toast.makeText(
            context,
            context!!.getString(R.string.no_scanning),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onTextNotFound() {
        Toast.makeText(
            context,
            context!!.getString(R.string.no_scanning),
            Toast.LENGTH_SHORT
        ).show()
    }

    // Deprecation suppressed because we use an old API version
    @Suppress("DEPRECATION")
    fun onScanSuccessful() {
        // Checks vibration setting and then Vibrate or not vibrate
        if (vibrator.hasVibrator() && presenter.getVibrationSetting()) {
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

    override fun onProductNameEdited(productDictionary: ProductDictionary, insertLocal: Boolean) {
        productDictionary.productName?.let { onProductNameCheckboxChecked(it) }
        this.barcodeDictionary = productDictionary
        PreviewAnalyzer.hasBarCode = true
        ibRefreshProductName.visibility = View.VISIBLE
        toggleAddVittleButton()
        if (insertLocal) {
            presenter.insertProductDictionary(productDictionary)
        }
    }

    override fun onExpirationDateEdited(text: String) {
        onExpirationDateCheckboxChecked(text)
        PreviewAnalyzer.hasExpirationDate = true
        ibRefreshDate.visibility = View.VISIBLE
        toggleAddVittleButton()
    }

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

    override fun onResetDate() {
        tvExpirationDate.text = getString(R.string.date_format_scanner)
        ivCheckboxExpirationDate.setImageDrawable(
            context?.let {
                getDrawable(
                    it,
                    R.drawable.ic_circle
                )
            }
        )
        PreviewAnalyzer.hasExpirationDate = false
        this.expirationDate = null
        ibRefreshDate.visibility = View.INVISIBLE
        toggleAddVittleButton()
    }

    override fun onResetProductName() {
        tvProductName.text = getString(R.string.product_name_scanner)
        ivCheckboxBarcode.setImageDrawable(
            context?.let {
                getDrawable(
                    it,
                    R.drawable.ic_circle
                )
            }
        )
        PreviewAnalyzer.hasBarCode = false
        ibRefreshProductName.visibility = View.INVISIBLE
        toggleAddVittleButton()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (presenter.allPermissionsGranted()) {
                textureView.post { presenter.startCamera() }
                btnUseCamera.visibility = View.GONE
                btnTorch.visibility = View.VISIBLE
            } else {
                onNoPermissionGranted()
            }
        }
    }

    override fun onRequestPermissionsFromFragment() {
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    override fun onNoPermissionGranted() {
        btnUseCamera.visibility = View.VISIBLE
        btnTorch.visibility = View.GONE
    }

    override fun onEditNameButtonClick() {
        onShowEditNameDialog(barcodeDictionary)
    }

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

    override fun onShowEditNameDialog(productDictionary: ProductDictionary) {
        val dialog = ProductNameEditView(onFinished = { productName: String, insertLocal: Boolean ->
            onProductNameEdited(
                ProductDictionary(productDictionary.barcode, productName),
                insertLocal
            )
        })
        context?.let { dialog.openDialog(it, productDictionary.productName) }
    }

    override fun onResetView() {
        onResetDate()
        onResetProductName()
    }

    override fun onShowAddProductError() {
        createSnackbar(
            requireContext(),
            layout,
            getString(R.string.product_name_invalid),
            Gravity.CENTER_HORIZONTAL,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onShowAddProductSucceed() {
        createSnackbar(
            requireContext(),
            layout,
            getString(R.string.product_added),
            Gravity.CENTER_HORIZONTAL,
            Snackbar.LENGTH_SHORT
        ).show()    }

    override fun onShowExpirationPopup(product: Product) {
        if (product.getDaysRemaining() > DAYS_REMAINING_EXPIRED) {
            onShowCloseToExpirationPopup(product)
        } else {
            onShowAlreadyExpiredPopup(product)
        }
    }

    override fun onShowCloseToExpirationPopup(product: Product) {
        val multipleDaysChar = if (product.getDaysRemaining() == 1) {
            ""
        } else {
            "s"
        }

        context?.let {
            PopupManager.instance.showPopup(
                it,
                PopupBase(
                    getString(R.string.close_to_expiration_header),
                    getString(
                        R.string.close_to_expiration_subText,
                        product.getDaysRemaining(),
                        multipleDaysChar
                    )
                ),
                PopupButton(getString(R.string.btn_no).toUpperCase(Locale.getDefault())),
                PopupButton(getString(R.string.btn_yes).toUpperCase(Locale.getDefault())) {
                    presenter.addProductToList(
                        product,
                        false
                    )
                }
            )
        }
    }

    override fun onShowAlreadyExpiredPopup(product: Product) {
        context?.let {
            PopupManager.instance.showPopup(
                it,
                PopupBase(
                    getString(R.string.already_expired_header),
                    getString(R.string.already_expired_subText)
                ),
                PopupButton(getString(R.string.btn_no).toUpperCase(Locale.getDefault())),
                PopupButton(getString(R.string.btn_yes).toUpperCase(Locale.getDefault())) {
                    presenter.addProductToList(
                        product,
                        false
                    )
                }
            )
        }
    }

    private fun onBackPressed() {
        val action = Runnable {
            when (args.previousFragment) {
                PreviousFragmentIndex.PRODUCT_LIST() -> findNavController().navigate(
                    NavigationGraphDirections.actionGlobalProductListFragment(null, false)
                )
                PreviousFragmentIndex.SETTINGS() -> findNavController().navigate(
                    NavigationGraphDirections.actionGlobalSettingsFragment()
                )
                else -> findNavController().navigateUp()
            }
        }
        if ((PreviewAnalyzer.hasBarCode || PreviewAnalyzer.hasExpirationDate)) {
            showLeaveWarning {
                action.run()
            }
        } else {
            action.run()
        }
    }

    private fun showLeaveWarning(onLeave: () -> Unit) {
        PopupManager.instance.showPopup(
            requireContext(),
            PopupBase(
                getString(R.string.leave_scanner_header),
                getString(R.string.leave_scanner_subText)
            ),
            PopupButton(getString(R.string.btn_no).toUpperCase(Locale.getDefault())),
            PopupButton(getString(R.string.btn_yes).toUpperCase(Locale.getDefault())) {
                onLeave.invoke()
            }
        )
    }

    override fun onAnalyzerError() {
        Handler(Looper.getMainLooper()).post {
            PopupManager.instance.showPopup(this.context!!, PopupBase(
                getString(R.string.scanner_oom_title),
                getString(R.string.scanner_oom_text)
            ),
                PopupButton(getString(R.string.btn_no)),
                PopupButton(getString(R.string.btn_yes)) {
                    presenter.onPerformanceSettingLowered()
                    val currentFragment =
                        requireActivity().supportFragmentManager.primaryNavigationFragment!!
                            .childFragmentManager.fragments.first()
                    if (currentFragment is ScannerFragment) {
                        val fragTransaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        fragTransaction.detach(currentFragment)
                        fragTransaction.attach(currentFragment)
                        fragTransaction.commit()
                    }
                })
        }
    }

    companion object {

    /* This offset is used to counter the default values from the Date object. */
        const val MONTHS_OFFSET = 1
    }
}
