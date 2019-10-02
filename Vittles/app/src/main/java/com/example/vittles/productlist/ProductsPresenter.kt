package com.example.vittles.productlist

import com.example.domain.productfetch.FetchProductsUseCase
import com.example.vittles.mvp.BasePresenter
import com.example.vittles.productlist.ProductsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductsPresenter @Inject internal constructor(private val fetchProductsUseCase: FetchProductsUseCase) : BasePresenter<ProductsActivity>(){

    fun loadProducts() {
        disposables.add(fetchProductsUseCase.fetch().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductsLoad(it) }, { view?.onProductsLoadFail()}))
    }
}