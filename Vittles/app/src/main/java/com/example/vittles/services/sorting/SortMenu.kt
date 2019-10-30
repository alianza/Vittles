package com.example.vittles.services.sorting

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.productlist.ProductAdapter
import kotlinx.android.synthetic.main.activity_sort.view.*

/**
 * Class for the sorting Menu, This menu shows all the sorting types and the current sorting type.
 *
 * @author Marc van Spronsen
 *
 * @property currentSortingType The current sorting type that is used.
 * @property sortList The list that should be sorted.
 * @property adapter the adapter for the list that should be sorted.
 */
class SortMenu (sortList: MutableList<Product>, adapter: ProductAdapter) {

    private var currentSortingType: SortingType = SortingType.DAYS_REMAINING_ASC
    private var sortList = sortList
    private var adapter = adapter

    /**
     * Sets enums for all of the different sorting options
     *
     */
    enum class SortingType() {
        DAYS_REMAINING_ASC,
        DAYS_REMAINING_DESC,
        ALFABETIC_AZ,
        ALFABETIC_ZA,
        NEWEST,
        OLDEST
    }

    /**
     * Inflates the sortingMenu.
     *
     * @param context The context in which the sortingMenu is active.
     * @param button The button which shows the current sortingType.
     */
    fun openMenu(context: Context, button: Button) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.activity_sort, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        setCircleColor(mDialogView)
        setListeners(mDialogView, mAlertDialog, button)
    }

    /**
     * Sets all the onClickListeners
     *
     * @param view The View which holds the sortingMenu.
     * @param alertDialog The entire alertdialog of the sortMenu.
     * @param button The button which shows the current sortingType.
     */
    private fun setListeners(view: View, alertDialog: AlertDialog, button: Button) {
        view.daysRemainingLH.setOnClickListener { onSortClick(SortingType.DAYS_REMAINING_ASC, view, alertDialog, button)  }
        view.daysRemainingHL.setOnClickListener { onSortClick(SortingType.DAYS_REMAINING_DESC, view, alertDialog, button) }
        view.alfabeticAZ.setOnClickListener { onSortClick(SortingType.ALFABETIC_AZ, view, alertDialog, button) }
        view.alfabeticZA.setOnClickListener { onSortClick(SortingType.ALFABETIC_ZA, view, alertDialog, button) }
        view.newest.setOnClickListener { onSortClick(SortingType.NEWEST, view, alertDialog, button) }
        view.oldest.setOnClickListener { onSortClick(SortingType.OLDEST, view, alertDialog, button) }
    }

    /**
     * Handles all actions that happen when a button is clicked
     *
     * @param sortingType The sortingType which was selected by the user.
     * @param view The View which holds the sortingMenu.
     * @param alertDialog The entire alertdialog of the sortMenu.
     * @param button The button which shows the current sortingType.
     */
    private fun onSortClick(sortingType: SortingType, view: View, alertDialog: AlertDialog, button: Button) {
        when(sortingType) {
            SortingType.DAYS_REMAINING_ASC -> sortList.sortBy { it.expirationDate }
            SortingType.DAYS_REMAINING_DESC -> sortList.sortByDescending { it.expirationDate }
            SortingType.ALFABETIC_AZ -> sortList.sortBy { it.productName }
            SortingType.ALFABETIC_ZA -> sortList.sortByDescending { it.productName }
            SortingType.NEWEST -> sortList.sortByDescending { it.creationDate }
            else -> sortList.sortBy { it.creationDate }
        }
        setSortbtnText(sortingType, view, button)
        currentSortingType = sortingType
        adapter.products = sortList
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

    /**
     * Sets the buttonText to the current sorting method.
     *
     * @param sortingType The sortingType which was selected by the user.
     * @param view The View in which the button is active.
     * @param btnSort The button of which the text needs to be replaced.
     */
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