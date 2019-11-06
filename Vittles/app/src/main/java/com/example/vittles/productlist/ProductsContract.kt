package com.example.vittles.productlist

import com.example.domain.product.Product
import com.example.vittles.enums.DeleteType

/**
 * MVP Contract for products overview.
 *
 * @author Jeroen Flietstra
 */
interface ProductsContract {

    interface View {
        fun initViews()
        fun setListeners()
        fun setItemTouchHelper()
        fun setEmptyView()
        fun onAddButtonClick()
        fun filter(query: String)
        fun setNoResultsView()
//        fun openSearchBar()
//        fun closeSearchBar()
        fun populateRecyclerView()
        fun showProducts(products: List<Product>)
        fun showProductDeleteError()

    }

    interface Presenter {
        fun startPresenting()
        fun loadIndicationColors(products: List<Product>)
        fun deleteProduct(product: Product, deleteType: DeleteType)
    }
}