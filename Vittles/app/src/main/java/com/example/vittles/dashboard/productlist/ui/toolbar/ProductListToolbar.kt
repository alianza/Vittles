package com.example.vittles.dashboard.productlist.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.domain.product.model.ProductSortingType
import com.example.vittles.R
import com.example.vittles.dashboard.productlist.SortingTypeTextProvider
import com.example.vittles.extension.setGone
import com.example.vittles.extension.setVisible
import com.example.vittles.services.popups.SingleChoiceMenu
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.toolbar_productlist.view.*

/**
 * @author Jeroen Flietstra
 *
 */
class ProductListToolbar(context: Context, attrs: AttributeSet) : AppBarLayout(context, attrs),
    SearchView.OnQueryTextListener {

    var productListToolbarListener: ProductListToolbarListener? = null

    private var provider: SortingTypeTextProvider? = null

    var sortingType = ProductSortingType.DAYS_REMAINING_ASC
        private set(value) {
            field = value; productListToolbarListener?.onSortingTypeChanged(value)
        }

    var parent: Fragment? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.toolbar_productlist, this)
        svSearch.setOnQueryTextListener(this)
        svSearch.setOnCloseListener { closeSearchInput() }
        ibSearch.setOnClickListener { openSearchInput() }
        ibCloseSearch.setOnClickListener { closeSearchInput() }
        btnSort.setOnClickListener {
            parent?.let {
                provider = SortingTypeTextProvider(it.requireContext())
                SingleChoiceMenu(
                    provider!!,
                    this::onSortingTypeSelected,
                    sortingType,
                    ProductSortingType.values(),
                    R.layout.dialog_sort
                )
                    .show(it.requireFragmentManager(), TAG)
            }
        }
    }

    fun isSearching(): Boolean = searchLayout.isVisible

    fun openSearchInput(): Boolean {
        plToolbar.setGone()
        searchLayout.setVisible()
        svSearch.isIconified = false
        svSearch.setQuery("", true)
        return true
    }

    private fun closeSearchInput(): Boolean {
        svSearch.setQuery("", true)
        searchLayout.setGone()
        plToolbar.setVisible()
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        productListToolbarListener?.onQueryTextChanged(p0)
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return onQueryTextSubmit(p0)
    }

    private fun onSortingTypeSelected(sortingType: ProductSortingType) {
        provider?.let {
            this.sortingType = sortingType
            btnSort.tvSortType.text = it.getText(sortingType)
        }
    }

    interface ProductListToolbarListener {
        fun onSortingTypeChanged(sortingType: ProductSortingType)
        fun onQueryTextChanged(query: String?)
    }

    companion object {

        const val TAG = "PRODUCT_LIST_TOOLBAR"
    }
}