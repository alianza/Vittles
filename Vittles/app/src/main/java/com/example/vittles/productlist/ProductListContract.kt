package com.example.vittles.productlist

import com.example.vittles.enums.DeleteType

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
    }
}