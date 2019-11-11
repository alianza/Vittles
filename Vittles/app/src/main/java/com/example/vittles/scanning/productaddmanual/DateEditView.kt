package com.example.vittles.scanning.productaddmanual

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.example.vittles.R

/**
 * Dialog for editing the expiration date.
 *
 * @author Jeroen Flietstra
 * @author Marc van Spronsen
 */
class DateEditView {

    /**
     * Opens the dialog.
     *
     * @param context The application context.
     * @param textView The text view with the date value.
     */
    fun openDialog(context: Context, textView: TextView) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_date_edit, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
    }
}