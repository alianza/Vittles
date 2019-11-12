package com.example.vittles.scanning.productaddmanual

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
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

    /**
     * Opens the dialog.
     *
     * @param context The application context.
     */
    @SuppressLint("InflateParams")
    fun openDialog(context: Context) {
        view =
            LayoutInflater.from(context).inflate(R.layout.dialog_productname_edit, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(view)
        dialog = mBuilder.show()

        view.btnConfirm.setOnClickListener { onConfirmButtonClicked() }
        view.btnCancel.setOnClickListener { onCancelButtonClicked() }
    }

    private fun onConfirmButtonClicked() {
        onFinished(view.etProductName.text.toString())
        dialog.dismiss()
    }

    private fun onCancelButtonClicked() {
        dialog.dismiss()
    }
}