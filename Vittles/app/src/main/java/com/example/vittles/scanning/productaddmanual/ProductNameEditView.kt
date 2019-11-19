package com.example.vittles.scanning.productaddmanual

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.vittles.R
import kotlinx.android.synthetic.main.dialog_productname_edit.view.*

/**
 * Dialog for editing the product name.
 *
 * @author Jeroen Flietstra
 *
 * @property onFinished Callback function on finished name change.
 */
class ProductNameEditView(private val onFinished: (productName: String) -> Unit) {

    private lateinit var view: View
    private lateinit var dialog: AlertDialog
    private lateinit var context: Context

    /**
     * Opens the dialog.
     *
     * @param context The application context.
     */
    @SuppressLint("InflateParams")
    fun openDialog(context: Context, productName: String?) {
        this.context = context
        view =
            LayoutInflater.from(context).inflate(R.layout.dialog_productname_edit, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(view)
        dialog = mBuilder.show()

        if (!productName.isNullOrBlank() && productName != context.getString(R.string.product_name_scanner)) {
            view.etProductName.setText(productName)
        }

        view.btnConfirm.setOnClickListener { onConfirmButtonClicked() }
        view.btnCancel.setOnClickListener { onCancelButtonClicked() }
    }

    /**
     * Returns the correct value in the callback.
     *
     */
    private fun onConfirmButtonClicked() {
        if (!view.etProductName.text.isNullOrBlank()) {
            onFinished(view.etProductName.text.toString())
            // Close the keyboard
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            dialog.dismiss()
        } else {
            Toast.makeText(context, context.getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Dismisses the dialog.
     *
     */
    private fun onCancelButtonClicked() {
        // Close the keyboard
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        dialog.dismiss()
    }
}