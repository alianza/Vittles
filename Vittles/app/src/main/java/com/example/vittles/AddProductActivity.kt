package com.example.vittles

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.vittles.data.AppDatabase
import com.example.vittles.data.ProductDao
import com.example.vittles.model.Product
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_product.*
import java.util.*

/**
 * Activity class for the Add Product component. This components makes it possible to
 * create a product manually and insert it into the local database.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 */
@Suppress("DEPRECATION") // Suppress deprecation on 'Date' since the project is running on API 21.
class AddProductActivity : AppCompatActivity() {
    private lateinit var productDao: ProductDao

    private val calendar = Calendar.getInstance()
    private var expirationDate = Date()

    companion object{
        /**
         * These offsets are used to counter the default values from the Date object.
         *
         */
        const val YEARS_OFFSET = 1900
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
        productDao = AppDatabase.getDatabase(applicationContext).productDao()
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
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        etExpirationDate.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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
    private fun onConfirmButtonClick() {
        if (validate()) {
            val product = Product(
                null,
                etProductName.text.toString(),
                this.expirationDate,
                calendar.time
            )
            val status = productDao.insert(product)
            if (status < 0) {
                Snackbar.make(layout, getString(R.string.product_failed), Snackbar.LENGTH_LONG)
                    .show()
            } else {
                etProductName.setText("")
                etExpirationDate.setText("")
            }
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
}
