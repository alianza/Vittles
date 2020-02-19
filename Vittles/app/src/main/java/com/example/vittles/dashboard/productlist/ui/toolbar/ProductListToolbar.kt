package com.example.vittles.dashboard.productlist.ui.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.vittles.R
import com.example.vittles.dashboard.model.ProductSortingType
import com.example.vittles.dashboard.productlist.SortingTypeTextProvider
import com.example.vittles.dashboard.productlist.ui.list.ProductAdapter
import com.example.vittles.extension.setGone
import com.example.vittles.extension.setVisible
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.toolbar_productlist.view.*

class ProductListToolbar (context: Context, attrs: AttributeSet) : AppBarLayout(context, attrs),
    SearchView.OnQueryTextListener {

    private var provider: SortingTypeTextProvider? = null
    private var sortingType = ProductSortingType.DAYS_REMAINING_ASC

    var adapter: ProductAdapter? = null
        set(value) {
            if (value != null) {
                field = value
            }
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
                ProductListToolbarSortMenu(
                    provider!!,
                    this::onSortingTypeSelected,
                    sortingType
                )
                .show(it.requireFragmentManager(), TAG)
            }
        }
    }

    fun openSearchInput(): Boolean {
        return if (adapter == null) {
            false
        } else {
            plToolbar.setGone()
            searchLayout.setVisible()
            svSearch.isIconified = false
            svSearch.setQuery("", true)
            true
        }
    }

    fun closeSearchInput(): Boolean {
        return if (adapter == null) {
            false
        } else {
            svSearch.setQuery("", true)
            searchLayout.setGone()
            plToolbar.setVisible()
            true
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return if (adapter == null) {
            false
        } else {
            adapter?.filter?.filter(p0)
            true
        }
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return onQueryTextSubmit(p0)
    }

    private fun onSortingTypeSelected(sortingType: ProductSortingType) {
        adapter?.let { adapter ->
            provider?.let {
                this.sortingType = sortingType
                adapter.sort(sortingType)
                btnSort.tvSortType.text = it.getSortingTypeText(sortingType)
            }
        }
    }

    companion object {
        const val TAG = "PRODUCT_LIST_TOOLBAR"
    }
}