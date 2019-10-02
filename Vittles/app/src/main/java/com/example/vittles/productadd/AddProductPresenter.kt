package com.example.vittles.productadd

import com.example.domain.model.Product
import com.example.domain.productadd.AddProductUseCase
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * This is the presenter for the add product activity.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property addProductUseCase The AddProductUseCase from the domain module.
 */
class AddProductPresenter @Inject internal constructor(private val addProductUseCase: AddProductUseCase) : BasePresenter<AddProductActivity>() {

    /**
     * Method used to add a product.
     *
     * @param product The product to add.
     */
    fun addProduct(product: Product) {
        disposables.add(addProductUseCase.add(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductAddSucceed() }, {view?.onProductAddFail()}))
    }
}