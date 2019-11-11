package com.example.vittles.services.sorting

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.domain.product.Product
import com.example.vittles.R
import com.example.vittles.productlist.ProductAdapter
import kotlinx.android.synthetic.main.dialog_sort.view.*

/**
 * Class for the sorting Menu, This menu shows all the sorting types and the current sorting type.
 *
 * @author Marc van Spronsen
 *
 * @property currentSortingType The current sorting type that is used.
 * @property previousSortingType The previous sorting type that was used.
 * @property sortList The list that should be sorted.
 * @property adapter the adapter for the list that should be sorted.
 * @property btnSort The button which shows the current sortingType.
 * @property alertDialog The entire alertDialog of the sortMenu.
 * @property view The View which holds the sortingMenu.
 */
class SortMenu (sortList: MutableList<Product>, adapter: ProductAdapter) {

    private var currentSortingType: SortingType = SortingType.DAYS_REMAINING_ASC
    private var adapter = adapter
    private var sortList = sortList
    lateinit var previousSortingType: SortingType
    lateinit var btnSort: TextView
    lateinit var alertDialog: AlertDialog
    lateinit var view: View

    /**
     * Sets enums for all of the different sorting options
     *
     */
    enum class SortingType {
        DAYS_REMAINING_ASC,
        DAYS_REMAINING_DESC,
        ALPHABETIC_AZ,
        ALPHABETIC_ZA,
        NEWEST,
        OLDEST
    }

    /**
     * Inflates the sortingMenu.
     *
     * @param context The context in which the sortingMenu is active.
     * @param button The button which shows the current sortingType.
     * @param filteredList The list that should be sorted.
     */
    fun openMenu(context: Context, button: TextView, filteredList: MutableList<Product>) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_sort, null)
        val mBuilder =
            AlertDialog.Builder(context).setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        btnSort = button
        alertDialog = mAlertDialog
        view = mDialogView
        sortList = filteredList

        setCircleColor()
        setListeners()
    }

    /**
     * Called when text is entered in the search view.
     *
     * @param sortList The list that should be sorted.
     */
    fun sortFilteredList(sortList: MutableList<Product>) {
        onSortClick(currentSortingType, sortList)
    }

    /**
     * Sets all the onClickListeners
     *
     */
    private fun setListeners() {
        view.daysRemainingLH.setOnClickListener { onSortClick(SortingType.DAYS_REMAINING_ASC, sortList)  }
        view.daysRemainingHL.setOnClickListener { onSortClick(SortingType.DAYS_REMAINING_DESC, sortList) }
        view.alfabeticAZ.setOnClickListener { onSortClick(SortingType.ALPHABETIC_AZ, sortList) }
        view.alfabeticZA.setOnClickListener { onSortClick(SortingType.ALPHABETIC_ZA, sortList) }
        view.newest.setOnClickListener { onSortClick(SortingType.NEWEST, sortList) }
        view.oldest.setOnClickListener { onSortClick(SortingType.OLDEST, sortList) }
    }

    /**
     * Handles all actions that happen when a button is clicked
     *
     * @param sortingType The sortingType which was selected by the user.
     * @param sortList The list that should be sorted.
     */
    private fun onSortClick(sortingType: SortingType, sortList: MutableList<Product>) {
        when(sortingType) {
            SortingType.DAYS_REMAINING_ASC -> sortList.sortBy { it.expirationDate }
            SortingType.DAYS_REMAINING_DESC -> sortList.sortByDescending { it.expirationDate }
            SortingType.ALPHABETIC_AZ -> sortList.sortBy { it.productName }
            SortingType.ALPHABETIC_ZA -> sortList.sortByDescending { it.productName }
            SortingType.NEWEST -> sortList.sortByDescending { it.creationDate }
            else -> sortList.sortBy { it.creationDate }
        }
        previousSortingType = currentSortingType
        currentSortingType = sortingType
        adapter.products = sortList
        adapter.notifyDataSetChanged()

        if (currentSortingType != previousSortingType) {
            setSortbtnText(sortingType)
        }

        if (::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }
    }

    /**
     * Sets the circle alpha to 1 of the selected sorting method.
     *
     */
    private fun setCircleColor() {
        when (currentSortingType) {
            SortingType.DAYS_REMAINING_ASC -> view.daysRemainingAsc.alpha = 1f
            SortingType.DAYS_REMAINING_DESC -> view.daysRemainingDesc.alpha = 1f
            SortingType.ALPHABETIC_AZ -> view.alfabeticAz.alpha = 1f
            SortingType.ALPHABETIC_ZA -> view.alfabeticZa.alpha = 1f
            SortingType.NEWEST -> view.newestSelected.alpha = 1f
            else -> view.oldestSelected.alpha = 1f
        }
    }

    /**
     * Sets the buttonText to the current sorting method.
     *
     * @param sortingType The sortingType which was selected by the user.
     */
    private fun setSortbtnText(sortingType: SortingType) {
        when(sortingType) {
            SortingType.DAYS_REMAINING_ASC -> btnSort.text = view.daysRemainingLH.text
            SortingType.DAYS_REMAINING_DESC -> btnSort.text = view.daysRemainingHL.text
            SortingType.ALPHABETIC_AZ -> btnSort.text = view.alfabeticAZ.text
            SortingType.ALPHABETIC_ZA -> btnSort.text = view.alfabeticZA.text
            SortingType.NEWEST -> btnSort.text = view.newest.text
            else -> btnSort.text = view.oldest.text
        }
    }
}