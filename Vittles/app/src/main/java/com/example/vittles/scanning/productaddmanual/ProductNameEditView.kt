package com.example.vittles.scanning.productaddmanual

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.domain.product.ProductDictionaryStatus
import com.example.vittles.R
import com.example.vittles.extension.dismissKeyboard
import com.example.vittles.extension.showKeyboard
import kotlinx.android.synthetic.main.dialog_productname_edit.view.*
import java.util.*
import kotlin.concurrent.schedule

/**
 * Dialog for editing the product name.
 *
 * @author Jeroen Flietstra
 *
 * @property onFinished Callback function on finished name change.
 */
class ProductNameEditView(
    private val onFinished: (productName: String, insertLocal: Boolean) -> Unit
) {

    /** @suppress */
    private lateinit var view: View
    /** @suppress */
    private lateinit var dialog: AlertDialog
    /** @suppress */
    private lateinit var context: Context
    /** @suppress */
    private var insertLocal: Boolean = false

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

        dialog.setCancelable(false)
        
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onCancelButtonClicked()
            }
            return@setOnKeyListener true
        }

        if (!productName.isNullOrBlank()
            && productName != context.getString(R.string.product_name_scanner)
            && productName != ProductDictionaryStatus.NOT_FOUND()
            && productName != ProductDictionaryStatus.NOT_READY()) {
            view.etProductName.setText(productName)
        }

        view.tvMessage.visibility =
            if (productName == ProductDictionaryStatus.NOT_FOUND()) TextView.VISIBLE else TextView.GONE
        insertLocal = productName == ProductDictionaryStatus.NOT_FOUND()

        view.etProductName.requestFocus()

        // Show keyboard after delay because of new activity
        Timer("showKeyboard", false).schedule(500) {
            showKeyboard(
                context,
                view
            )
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
            onFinished(view.etProductName.text.toString(), insertLocal)
            dismissKeyboard(context, view)
            dialog.dismiss()
        } else {
            Toast.makeText(context, context.getString(R.string.empty_fields), Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Dismisses the dialog.
     *
     */
    private fun onCancelButtonClicked() {
        dismissKeyboard(context, view)
        dialog.dismiss()
    }
}