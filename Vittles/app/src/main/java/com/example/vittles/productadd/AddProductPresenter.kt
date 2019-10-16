package com.example.vittles.productadd

import com.example.domain.product.Product
import com.example.domain.product.AddProduct
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * This is the presenter for the invoke product activity.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property addProduct The AddProduct from the domain module.
 */
class AddProductPresenter @Inject internal constructor(private val addProduct: AddProduct) : BasePresenter<AddProductActivity>() {

    /**
     * Method used to add a product.
     *
     * @param product The product to add.
     */
    fun addProduct(product: Product) {

        //Subscribe to the onProductsCloseToExpiring event.
        addProductUseCase.onProductCloseToExpiring += { view?.showCloseToExpirationPopup(product.getDaysRemaining()) }

        disposables.add(
            addProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductAddSucceed() }, {view?.onProductAddFail()}))

        //Unsubscribe from the onProductsCloseToExpiring event.
        addProductUseCase.onProductCloseToExpiring -= { view?.showCloseToExpirationPopup(product.getDaysRemaining()) }
    }
}