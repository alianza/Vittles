package com.example.vittles.productlist

import com.example.domain.product.DeleteProduct
import com.example.domain.product.Product
import com.example.domain.product.GetProducts
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
 * @property getProducts The GetProducts use case from the domain module
 */
class ProductsPresenter @Inject internal constructor(
    private val getProducts: GetProducts,
    private val deleteProduct: DeleteProduct
) :
    BasePresenter<ProductsActivity>(), ProductsContract.Presenter {

    /**
     * Loads the products.
     *
     */
    override fun startPresenting() {
        disposables.add(
            getProducts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.showProducts(it) }, { view?.setNoResultsView() })
        )
    }

    /**
     * Loads the indication colors for the products.
     *
     * @param products The list containing the products that are shown in the ListView.
     */
    override fun loadIndicationColors(products: List<Product>) {
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
    override fun deleteProduct(product: Product) {
        disposables.add(deleteProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.populateRecyclerView() }, { view?.showProductDeleteError() })
        )
    }
}