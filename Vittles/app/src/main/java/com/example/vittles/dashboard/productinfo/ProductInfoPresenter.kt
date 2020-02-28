package com.example.vittles.dashboard.productinfo

import com.example.domain.product.UpdateProduct
import com.example.vittles.mvp.BasePresenter
import com.example.vittles.dashboard.ProductMapper
import com.example.vittles.dashboard.model.ProductViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Arjen Simons
 */
class ProductInfoPresenter @Inject internal constructor(
    private val mapper: ProductMapper,
    private val updateProduct: UpdateProduct
) : BasePresenter<ProductInfoFragment>(),
    ProductInfoContract.Presenter {

    override fun onProductUpdate(product: ProductViewModel) {
        disposables.add(updateProduct(mapper.fromParcelable(product))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ view?.onProductUpdateSuccess() }, { view?.onProductUpdateFail() })
        )
    }
}