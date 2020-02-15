package com.example.vittles.productlist.productlist

import com.example.vittles.enums.DeleteType
import com.example.vittles.productlist.model.ProductViewModel

/**
 * MVP Contract for products overview.
 *
 * @author Jeroen Flietstra
 */
interface ProductListContract {

    interface View {
        fun onProductsUpdated(products: List<ProductViewModel>)
    }

    interface Presenter {
        fun startPresenting()
        fun onProductDelete(product: ProductViewModel, deleteType: DeleteType)
        fun onProductInsert(product: ProductViewModel)
    }
}