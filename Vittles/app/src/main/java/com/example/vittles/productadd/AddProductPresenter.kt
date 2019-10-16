package com.example.vittles.productadd

import com.example.domain.product.Product
import com.example.domain.product.AddProduct
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
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
     * @param checkDate If the date should be checked to show a popup.
     */
    fun addProduct(product: Product, checkDate: Boolean = true) {

        disposables.add(addProduct.invoke(product, checkDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductAddSucceed() },
                {
                    if (it is IllegalArgumentException){
                    view?.onProductAddFail() //Show Snackbar that tells it failed
                    }
                    else if (it is Exception){
                        view?.showCloseToExpirationPopup(product) //Show close to expiring popup
                    }
                }
            )
        )
    }
}