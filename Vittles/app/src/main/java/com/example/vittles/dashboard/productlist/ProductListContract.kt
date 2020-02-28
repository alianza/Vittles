package com.example.vittles.dashboard.productlist

import com.example.domain.product.model.ProductSortingType
import com.example.vittles.dashboard.model.ProductViewModel
import com.example.vittles.enums.DeleteType

/**
 * MVP Contract for products overview.
 *
 * @author Jeroen Flietstra
 */
interface ProductListContract {

    interface View {

        fun onProductsUpdated(products: List<ProductViewModel>)
        fun onShowLoadingView()
        fun onHideLoadingView()
    }

    interface Presenter {

        fun onListInitializeOrChange(sortingType: ProductSortingType, query: String = "")
        fun onProductDelete(product: ProductViewModel, deleteType: DeleteType)
        fun onProductInsert(product: ProductViewModel)
        fun onProductDeleted(deleteType: DeleteType)
    }
}