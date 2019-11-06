package com.example.vittles.scanning.productaddmanual

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.example.vittles.R

class ProductNameEditView {

    fun openDialog(context: Context, textView: TextView) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_productname_edit, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
    }
}