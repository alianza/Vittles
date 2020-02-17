package com.example.vittles.dashboard.productlist.ui.toolbar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.vittles.R
import com.example.vittles.dashboard.model.ProductSortingType
import com.example.vittles.dashboard.productlist.ProductListTextProvider
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class ProductListToolbarSortMenu @Inject constructor(
    private val provider: ProductListTextProvider
) : DaggerDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(it)
                // Set the dialog title
//            builder.setTitle(R.string.pick_toppings)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(
                    ProductSortingType.values().map(provider::getSortingTypeText).toTypedArray(), null
                ) { dialog, which, isChecked ->
                    if (isChecked) {
                        // If the user checked the item, add it to the selected items
                        selectedItems.add(which)
                    } else if (selectedItems.contains(which)) {
                        // Else, if the item is already in the array, remove it
                        selectedItems.remove(Integer.valueOf(which))
                    }
                }
            builder.create()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort, container, false)
    }
}