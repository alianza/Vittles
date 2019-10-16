package com.example.vittles.productlist

import com.example.domain.enums.ExpirationIndicationColor
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
class ProductsPresenter @Inject internal constructor(private val getProducts: GetProducts, private val deleteProduct: DeleteProduct) :
    BasePresenter<ProductsActivity>() {

    /**
     * Loads the products.
     *
     */
    fun loadProducts() {
        disposables.add(
            getProducts().subscribeOn(Schedulers.io())
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
            product.indicationColor = when (product.getIndicationColor()) {
                ExpirationIndicationColor.RED -> IndicationColor.RED.value
                ExpirationIndicationColor.YELLOW -> IndicationColor.YELLOW.value
                ExpirationIndicationColor.GREEN -> IndicationColor.GREEN.value
            }
        }
    }

    /**
     * Deletes a product.
     *
     * @param product The product that will be deleted.
     */
    fun deleteProduct(product : Product) {
        disposables.add(deleteProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductDeleteSucceed() }, {view?.onProductDeleteFail()}))
    }
}