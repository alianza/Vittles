package com.example.vittles.services.Sorting

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.productlist.ProductAdapter
import com.example.vittles.productlist.ProductsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sort.view.*


class SortMenu (mutableList: MutableList<Product>, adapter: ProductAdapter) {

    private var currentSortingType: SortingType = SortingType.DAYS_REMAINING_ASC
    private var products = mutableList
    private var adapter = adapter

    enum class SortingType() {
        DAYS_REMAINING_ASC,
        DAYS_REMAINING_DESC,
        ALFABETIC_AZ,
        ALFABETIC_ZA,
        NEWEST,
        OLDEST
    }

    fun openMenu(context: Context, button: Button) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.activity_sort, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        setCircleColor(mDialogView)
        setListeners(mDialogView, mAlertDialog, button)
    }

    private fun setListeners(view: View, alertDialog: AlertDialog, button: Button) {
        view.daysRemainingLH.setOnClickListener { onSortClick(SortingType.DAYS_REMAINING_ASC, view, alertDialog, button)  }
        view.daysRemainingHL.setOnClickListener { onSortClick(SortingType.DAYS_REMAINING_DESC, view, alertDialog, button) }
        view.alfabeticAZ.setOnClickListener { onSortClick(SortingType.ALFABETIC_AZ, view, alertDialog, button) }
        view.alfabeticZA.setOnClickListener { onSortClick(SortingType.ALFABETIC_ZA, view, alertDialog, button) }
        view.newest.setOnClickListener { onSortClick(SortingType.NEWEST, view, alertDialog, button) }
        view.oldest.setOnClickListener { onSortClick(SortingType.OLDEST, view, alertDialog, button) }
    }

    private fun onSortClick(sortingType: SortingType, view: View, alertDialog: AlertDialog, button: Button) {
        when(sortingType) {
            SortingType.DAYS_REMAINING_ASC -> products.sortBy { it.expirationDate }
            SortingType.DAYS_REMAINING_DESC -> products.sortByDescending { it.expirationDate }
            SortingType.ALFABETIC_AZ -> products.sortBy { it.productName }
            SortingType.ALFABETIC_ZA -> products.sortByDescending { it.productName }
            SortingType.NEWEST -> products.sortByDescending { it.creationDate }
            else -> products.sortBy { it.creationDate }
        }
        setSortbtnText(sortingType, view, button)
        currentSortingType = sortingType
        adapter.products = products
        adapter.notifyDataSetChanged()
        alertDialog.dismiss()
    }

    /**
     * Sets the circle alpha to 1 of the selected sorting method.
     *
     * @param view The View in which the circles are active.
     */
    private fun setCircleColor(view: View) {
        when (currentSortingType) {
            SortingType.DAYS_REMAINING_ASC -> view.daysRemainingAsc.alpha = 1f
            SortingType.DAYS_REMAINING_DESC -> view.daysRemainingDesc.alpha = 1f
            SortingType.ALFABETIC_AZ -> view.alfabeticAz.alpha = 1f
            SortingType.ALFABETIC_ZA -> view.alfabeticZa.alpha = 1f
            SortingType.NEWEST -> view.newestSelected.alpha = 1f
            else -> view.oldestSelected.alpha = 1f
        }
    }

    private fun setSortbtnText(sortingType: SortingType, view: View, btnSort: Button) {
        when(sortingType) {
            SortingType.DAYS_REMAINING_ASC -> btnSort.text = view.daysRemainingLH.text
            SortingType.DAYS_REMAINING_DESC -> btnSort.text = view.daysRemainingHL.text
            SortingType.ALFABETIC_AZ -> btnSort.text = view.alfabeticAZ.text
            SortingType.ALFABETIC_ZA -> btnSort.text = view.alfabeticZA.text
            SortingType.NEWEST -> btnSort.text = view.newest.text
            else -> btnSort.text = view.oldest.text
        }
    }
}