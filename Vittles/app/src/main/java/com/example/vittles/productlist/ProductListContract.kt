package com.example.vittles.productlist

import com.example.domain.product.Product
import com.example.vittles.enums.DeleteType

/**
 * MVP Contract for products overview.
 *
 * @author Jeroen Flietstra
 */
interface ProductListContract {

    interface View {
        fun initViews()
        fun setListeners()
        fun setItemTouchHelper()
        fun setEmptyView()
        fun onAddButtonClick()
        fun onNoResults()
        fun onSearchBarOpened()
        fun onSearchBarClosed()
        fun onPopulateRecyclerView()
        fun onShowProducts(products: List<Product>)
        fun onShowProductDeleteError()
        fun onSortMenuOpened()
        fun filter(query: String)
    }

    interface Presenter {
        fun startPresenting()
        fun loadIndicationColors(products: List<Product>)
        fun deleteProduct(product: Product, deleteType: DeleteType)
    }
}