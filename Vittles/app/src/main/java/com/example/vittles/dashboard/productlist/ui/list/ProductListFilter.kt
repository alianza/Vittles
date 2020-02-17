package com.example.vittles.dashboard.productlist.ui.list

import android.widget.Filter
import com.example.vittles.dashboard.model.ProductViewModel
import java.util.*
import kotlin.collections.ArrayList

class ProductListFilter(private val adapter: ProductAdapter) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val locale = Locale.getDefault()
        val filterString = constraint.toString().toLowerCase(locale)
        val filterResults = FilterResults()
        val results = arrayListOf<ProductViewModel>()
        if (constraint != null) {
            results.addAll(adapter.products.filter { it.productName.toLowerCase(locale).contains(filterString) })
        }
        filterResults.values = results
        filterResults.count = results.size
        return filterResults
    }

    @Suppress("UNCHECKED_CAST")
    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        val filteredData = results?.values as ArrayList<ProductViewModel>
        adapter.submitFilteredList(filteredData)
    }
}