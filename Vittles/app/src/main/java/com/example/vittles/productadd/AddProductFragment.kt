package com.example.vittles.productadd

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.domain.consts.DAYS_REMAINING_EXPIRED
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.scanning.ScanResult
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_add_product.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Fragment class for the Add Product component.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
class AddProductFragment : DaggerFragment(), AddProductContract.View {

    @Inject
    lateinit var presenter: AddProductPresenter

    private val args: AddProductFragmentArgs by navArgs()

    private var expirationDate = DateTime()

    private val formatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

    private lateinit var etProductName: EditText
    private lateinit var etExpirationDate: EditText
    private lateinit var btnConfirm: Button

    companion object {
        /**
         * This offset is used to counter the default values from the Date object.
         *
         */
        const val MONTHS_OFFSET = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this@AddProductFragment)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etProductName = view.findViewById(R.id.etProductName)
        etExpirationDate = view.findViewById(R.id.etExpirationDate)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**
     * Initializes the view elements including the back button, date picker and
     * button click listeners.
     *
     */
    override fun initViews() {
        initDatePicker()
        btnConfirm.setOnClickListener { onConfirmButtonClick() }
        btnScan.setOnClickListener { onScanButtonClick() }

        val scanResult = args.scanResult
        if (scanResult != null) {
            onScanResult(scanResult)
        }
    }

    /**
     * Initializes the date picker. Including the default date and listeners.
     *
     */
    override fun initDatePicker() {
        val currentDate = DateTime.now()

        val year = currentDate.year
        val month = currentDate.monthOfYear
        val day = currentDate.dayOfMonth

        etExpirationDate.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        this.expirationDate =
                            DateTime(year, monthOfYear + MONTHS_OFFSET, dayOfMonth, 0, 0)
                        etExpirationDate.setText(
                            getString(
                                R.string.expiration_format,
                                (this.expirationDate.dayOfMonth).toString(),
                                (this.expirationDate.monthOfYear).toString(),
                                (this.expirationDate.year).toString()
                            )
                        )
                    }, year, month - MONTHS_OFFSET, day
                )
            }
            if (dpd != null) {
                dpd.datePicker.minDate = currentDate.millis
                dpd.show()
            }
        }
    }

    /**
     * Processes the process of adding a product manually. Will call the validate method, then
     * create a product from the filled-in fields to finally try to insert it into the database.
     *
     */
    override fun onConfirmButtonClick() {
        if (validate()) {
            val product = Product(
                null,
                etProductName.text.toString(),
                this.expirationDate,
                DateTime.now(),
                null
            )
            presenter.addProduct(product)

        }
    }

    /**
     * If a scan result is returned, set the values in the input fields.
     *
     * @param scanResult The result from the ScannerActivity.
     */
    override fun onScanResult(scanResult: ScanResult) {
        etProductName.setText(scanResult.productName)
        etExpirationDate.setText(formatter.print(scanResult.expirationDate))
    }

    /**
     * Handles scan button click; opens scanning activity
     *
     */
    override fun onScanButtonClick() {
        NavHostFragment.findNavController(fragmentHost)
            .navigate(AddProductFragmentDirections.actionAddProductFragmentToScannerFragment())
    }

    /**
     * Validates the input fields from this activity. Will show feedback based on the validity.
     *
     * @return boolean that represents if the given values are valid.
     */
    override fun validate(): Boolean {
        return if (!TextUtils.isEmpty(etProductName.text) && !TextUtils.isEmpty(etExpirationDate.text)) {
            true
        } else {
            Snackbar.make(layout, getString(R.string.empty_fields), Snackbar.LENGTH_LONG).show()
            false
        }
    }

    /**
     * If product has been added, this method will reset the text fields.
     *
     */
    override fun onResetView() {
        etProductName.setText("")
        etExpirationDate.setText("")
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
}
