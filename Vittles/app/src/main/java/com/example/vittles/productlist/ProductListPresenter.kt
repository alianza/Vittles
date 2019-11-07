package com.example.vittles.productlist

import com.example.domain.enums.ExpirationIndicationColor
import com.example.domain.product.DeleteProduct
import com.example.domain.product.Product
import com.example.domain.product.GetProducts
import com.example.vittles.enums.DeleteType
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
 * @property getProducts The GetProducts use case from the domain module.
 */
class ProductListPresenter @Inject internal constructor(
    private val getProducts: GetProducts,
    private val deleteProduct: DeleteProduct
) :
    BasePresenter<ProductListFragment>(), ProductListContract.Presenter {

    /**
     * Loads the products.
     *
     */
    override fun startPresenting() {
        disposables.add(
            getProducts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.onShowProducts(it) }, { view?.onNoResults() })
        )
    }

    /**
     * Loads the indication colors for the products.
     *
     * @param products The list containing the products that are shown in the ListView.
     */
    override fun loadIndicationColors(products: List<Product>) {
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
     * @param deleteType The Delete Type, EATEN, THROWN_AWAY or REMOVED
     */
    override fun deleteProduct(product: Product, deleteType: DeleteType) {
        disposables.add(deleteProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onPopulateRecyclerView() }, { view?.onShowProductDeleteError() })
        )
    }
}