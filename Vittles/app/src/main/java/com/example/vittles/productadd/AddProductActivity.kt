package com.example.vittles.productadd

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import com.example.domain.model.Product
import com.example.vittles.R
import com.example.vittles.popups.PopupBase
import com.example.vittles.popups.PopupButton
import com.example.vittles.popups.PopupManager
import com.example.vittles.services.NotificationService
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_add_product.*
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * Activity class for the Add Product component. This components makes it possible to
 * create a product manually and insert it into the local database.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
class AddProductActivity : DaggerAppCompatActivity() {
    @Inject lateinit var presenter: AddProductPresenter

    private var expirationDate = DateTime()

    companion object{
        /**
         * This offset is used to counter the default values from the Date object.
         *
         */
        const val MONTHS_OFFSET = 1
    }

    /**
     * {@inheritDoc}
     * Sets the content, assigns the dao and calls the initViews method.
     *
     * @param savedInstanceState {@inheritDoc}
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start(this@AddProductActivity)
        setContentView(R.layout.activity_add_product)
        initViews()
    }

    /**
     * Initializes the view elements including the back button, date picker and
     * button click listeners.
     *
     */
    private fun initViews() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_product_title)
        initDatePicker()
        btnConfirm.setOnClickListener { onConfirmButtonClick() }
    }


    /**
     * {@inheritDoc}
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return onBackButtonClick()
    }

    /**
     * Terminates the add product activity.
     *
     * @return boolean that represents if action succeeded
     */
    private fun onBackButtonClick(): Boolean {
        finish()
        return true
    }

    /**
     * Initializes the date picker. Including the default date and listeners.
     *
     */
    private fun initDatePicker() {
        val currentDate = DateTime.now()

        val year = currentDate.year
        val month = currentDate.monthOfYear
        val day = currentDate.dayOfMonth

        etExpirationDate.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    this.expirationDate = DateTime(year, monthOfYear + MONTHS_OFFSET, dayOfMonth, 0, 0)
                            etExpirationDate.setText(getString(
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
    private fun onConfirmButtonClick() {
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
    private fun validate(): Boolean {
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
    fun onProductAddSucceed() {
        etProductName.setText("")
        etExpirationDate.setText("")
        NotificationService.createDataNotification(this@AddProductActivity,
            "hi",
            "This is the message of the notification when it is not expanded",
            "This is the message of the notification when it is expanded", false)

    }

    /**
     * If product could not be added, this method will create a feedback Snackbar for the error.
     *
     */
    fun onProductAddFail() {
        Snackbar.make(layout, getString(R.string.product_failed), Snackbar.LENGTH_LONG)
            .show()
    }

    /**
     * Shows the CloseToExpiring popup
     *
     * @param daysRemaining The amount of days until the product is expired
     */
    fun showCloseToExpirationPopup(daysRemaining: Int){
        PopupManager.instance.showPopup(
            this,
            PopupBase("Almost expired!", String.format("The scanned product expires in %d days ", daysRemaining), 5000),
            PopupButton("Dismiss")
        )
    }
}
