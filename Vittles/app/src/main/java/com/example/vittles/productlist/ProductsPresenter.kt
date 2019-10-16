package com.example.vittles.productlist

import com.example.domain.model.Product
import com.example.domain.productdelete.DeleteProductUseCase
import com.example.domain.productfetch.FetchProductsUseCase
import com.example.vittles.Globals
import com.example.vittles.enums.IndicationColor
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The presenter for the main product activity.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property fetchProductsUseCase The FetchProductUseCase from the domain module
 */
class ProductsPresenter @Inject internal constructor(private val fetchProductsUseCase: FetchProductsUseCase, private val deleteProductUseCase: DeleteProductUseCase) :
    BasePresenter<ProductsActivity>() {

    /**
     * Loads the products.
     *
     */
    fun loadProducts() {
        disposables.add(
            fetchProductsUseCase.fetch().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.onProductsLoadSucceed(it) }, { view?.onProductsLoadFail() })
        )
    }

    /**
     * Loads the indication colors for the products.
     *
     * @param products The list containing the products that are shown in the ListView.
     */
    fun loadIndicationColors(products: List<Product>) {
        products.forEach { product ->
            val daysRemaining = product.getDaysRemaining()
            product.indicationColor = when {
                daysRemaining < Globals.RED_COLOR_BOUNDARY -> IndicationColor.RED.value
                daysRemaining < Globals.YELLOW_COLOR_BOUNDARY -> IndicationColor.YELLOW.value
                else -> IndicationColor.GREEN.value
            }
        }
    }

    /**
     * Deletes a product.
     *
     * @param product The product that will be deleted.
     */
    fun deleteProduct(product : Product) {
        disposables.add(deleteProductUseCase.delete(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductDeleteSucceed() }, {view?.onProductDeleteFail()}))
    }
}