package com.example.vittles.productlist.productinfo

import com.example.domain.product.UpdateProduct
import com.example.vittles.mvp.BasePresenter
import com.example.vittles.productlist.ProductMapper
import com.example.vittles.productlist.ProductViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The presenter for the productInfo Fragment.
 *
 * @property onProductUpdate The UpdateProduct use case from the domain module.
 */
class ProductInfoPresenter @Inject internal constructor(
    private val mapper: ProductMapper,
    private val updateProduct: UpdateProduct
) : BasePresenter<ProductInfoFragment>(),
    ProductInfoContract.Presenter {

    /**
     * Updates a product.
     *
     * @param product The product to update (the id has to be the same as the one you want to update).
     */
    override fun onProductUpdate(product: ProductViewModel) {
        disposables.add(updateProduct(mapper.fromParcelable(product))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductUpdateSuccess() }, { view?.onProductUpdateFail() })
        )
    }
}