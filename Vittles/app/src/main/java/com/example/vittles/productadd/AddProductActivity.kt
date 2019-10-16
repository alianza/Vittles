package com.example.vittles.productadd

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.services.popups.PopupBase
import com.example.vittles.services.popups.PopupButton
import com.example.vittles.services.popups.PopupManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_product.*
import java.util.*
import javax.inject.Inject

/**
 * Activity class for the Add Product component. This components makes it possible to
 * create a product manually and insert it into the local database.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
@Suppress("DEPRECATION") // Suppress deprecation on 'Date' since the project is running on API 21.
class AddProductActivity : DaggerAppCompatActivity(), AddProductContract.View {
    @Inject
    lateinit var presenter: AddProductPresenter

    private val calendar = Calendar.getInstance()
    private var expirationDate = Date()

    companion object {
        /**
         * This offset is used to counter the default values from the Date object.
         *
         */
        const val YEARS_OFFSET = 1900
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
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        etExpirationDate.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    this.expirationDate = Date((year - YEARS_OFFSET), monthOfYear, dayOfMonth)
                    etExpirationDate.setText(
                        getString(
                            R.string.expiration_format,
                            this.expirationDate.date.toString(),
                            (this.expirationDate.month + MONTHS_OFFSET).toString(),
                            (this.expirationDate.year + YEARS_OFFSET).toString()
                        )
                    )
                }, year, month, day
            )
            dpd.datePicker.minDate = calendar.time.time
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
                calendar.time,
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
            Snackbar.make(layout, getString(R.string.product_added), Snackbar.LENGTH_SHORT).show()
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
     * Shows the CloseToExpiring popup.
     *
     */
    override fun showCloseToExpirationPopup(product: Product) {
        PopupManager.instance.showPopup(
            this,
            PopupBase(
                "Almost expired!",
                String.format("The scanned product expires in %d days ", product.getDaysRemaining())
            ),
            PopupButton("Add") { presenter.addProduct(product, false) },
            PopupButton("Don't add")
        )
    }
}
