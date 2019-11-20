package com.example.vittles.productinfo

import com.example.domain.product.DeleteProduct
import com.example.domain.product.Product
import com.example.vittles.enums.DeleteType
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductInfoPresenter @Inject internal constructor(
    private val deleteProduct: DeleteProduct
) : BasePresenter<ProductInfoFragment>(), ProductInfoContract.Presenter {

    /**
     * Updates a product
     *
     * @param product The product to update (the id has to be the same as the one you want to update)
     */
    override fun updateProduct(product: Product) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun deleteProduct(product: Product, deleteType: DeleteType) {
        disposables.add(deleteProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, { })
        )    }

}