package com.example.vittles.productadd

import com.example.domain.model.Product
import com.example.domain.productadd.AddProductUseCase
import com.example.vittles.mvp.BasePresenter
import com.example.vittles.productadd.AddProductActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddProductPresenter @Inject internal constructor(private val addProductUseCase: AddProductUseCase) : BasePresenter<AddProductActivity>() {

    fun addProduct(product: Product) {
        disposables.add(addProductUseCase.add(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductAdd() }, {}))
    }
}