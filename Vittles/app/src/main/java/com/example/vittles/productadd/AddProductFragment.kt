package com.example.vittles.productadd

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import com.example.domain.product.Product
import com.example.vittles.NavigationGraphDirections
import com.example.vittles.R
import com.example.vittles.scanning.SCAN_RESULT
import com.example.vittles.scanning.ScannerFragment
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

const val SCAN_PRODUCT_REQUEST_CODE = 100

/**
 * Activity class for the Add Product component. This components makes it possible to
 * create a product manually and insert it into the local database.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
class AddProductFragment : DaggerFragment(), AddProductContract.View {
    @Inject
    lateinit var presenter: AddProductPresenter

    private var expirationDate = DateTime()

    private val formatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

    lateinit var etExpirationDate: EditText
    lateinit var btnConfirm: Button

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

        // Will make it possible to go back to the previous screen with the phone's back button
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(fragmentHost).navigate(NavigationGraphDirections.actionGlobalProductListFragment(false))
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    /**
     * Retrieve the result from the activity.
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                SCAN_PRODUCT_REQUEST_CODE -> {
//                    val scanResult =
//                        data!!.getParcelableExtra<ScannerFragment.ScanResult>(SCAN_RESULT)
//                    etProductName.setText(scanResult?.productName)
//                    etExpirationDate.setText(formatter.print(scanResult?.expirationDate))
//                    expirationDate = scanResult?.expirationDate!!
//                }
//            }
//        }
    }

    /**
     * Temporary button action.
     *
     */
    private fun onScanButtonClick() {
//        val scannerActivity = Intent(
//            this,
//            ScannerFragment::class.java
//        )
//        startActivityForResult(scannerActivity, SCAN_PRODUCT_REQUEST_CODE)
        NavHostFragment.findNavController(fragmentHost).navigate(AddProductFragmentDirections.actionAddProductFragmentToScannerFragment())
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
            val dpd = DatePickerDialog(
                context!!,
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
            dpd.datePicker.minDate = currentDate.millis
            dpd.show()
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
        Snackbar.make(layout, getString(R.string.product_failed), Snackbar.LENGTH_LONG)
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
        PopupManager.instance.showPopup(
            context!!,
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
