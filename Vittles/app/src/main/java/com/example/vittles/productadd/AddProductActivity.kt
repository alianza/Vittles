package com.example.vittles.productadd

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.scanning.SCAN_RESULT
import com.example.vittles.scanning.ScannerActivity
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_product.*
import org.joda.time.DateTime
import javax.inject.Inject

const val SCAN_PRODUCT_REQUEST_CODE = 100

/**
 * Activity class for the Add Product component. This components makes it possible to
 * create a product manually and insert it into the local database.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
class AddProductActivity : DaggerAppCompatActivity(), AddProductContract.View {
    @Inject
    lateinit var presenter: AddProductPresenter

    private var expirationDate = DateTime()

    companion object {
        /**
         * This offset is used to counter the default values from the Date object.
         *
         */
        const val MONTHS_OFFSET = 1
    }

    /**
     * Sets the content, assigns the dao and calls the initViews method.
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start(this@AddProductActivity)
        setContentView(R.layout.activity_add_product)
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_product_title)
        initDatePicker()
        btnConfirm.setOnClickListener { onConfirmButtonClick() }
        btnScan.setOnClickListener { onScanButtonClick() }
    }

    /**
     * Retrieve the result from the activity.
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SCAN_PRODUCT_REQUEST_CODE -> {
                    val scanResult =
                        data!!.getParcelableExtra<ScannerActivity.ScanResult>(SCAN_RESULT)
                    etProductName.setText(scanResult?.productName)
                }
            }
        }
    }

    /**
     * Temporary button action.
     *
     */
    private fun onScanButtonClick() {
        val scannerActivity = Intent(
            this,
            ScannerActivity::class.java
        )
        startActivityForResult(scannerActivity, SCAN_PRODUCT_REQUEST_CODE)
    }


    /**
     * Listener of the back button.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return onBackButtonClick()
    }

    /**
     * Terminates the add product activity.
     *
     * @return boolean that represents if action succeeded
     */
    override fun onBackButtonClick(): Boolean {
        finish()
        return true
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
                this,
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
    override fun resetView() {
        etProductName.setText("")
        etExpirationDate.setText("")
    }

    /**
     * If product could not be added, this method will create a feedback snack bar for the error.
     *
     */
    override fun showAddProductError() {
        Snackbar.make(layout, getString(R.string.product_failed), Snackbar.LENGTH_LONG)
            .show()
    }

    /**
     * If product is added successfully, this method will show a toast displaying a success state.
     *
     */
    override fun showAddProductSucceed() {
        Snackbar.make(layout, getString(R.string.product_added), Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Shows the CloseToExpiring popup.
     *
     */
    override fun showCloseToExpirationPopup(product: Product) {
        PopupManager.instance.showPopup(
            this,
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
