package com.example.vittles.productinfo

import com.example.domain.product.DeleteProduct
import com.example.domain.product.Product
import com.example.domain.product.UpdateProduct
import com.example.vittles.enums.DeleteType
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The presenter for the productInfo Fragment.
 *
 * @property deleteProduct The DeleteProduct use case from the domain module.
 * @property updateProduct The UpdateProduct use case from the domain module.
 */
class ProductInfoPresenter @Inject internal constructor(
    private val deleteProduct: DeleteProduct,
    private val updateProduct: UpdateProduct
) : BasePresenter<ProductInfoFragment>(), ProductInfoContract.Presenter {

    /**
     * Updates a product.
     *
     * @param product The product to update (the id has to be the same as the one you want to update).
     */
    override fun updateProduct(product: Product) {
        disposables.add(updateProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, { })
        )
    }

    /**
     * Deletes a product.
     *
     * @param product The product to delete.
     * @param deleteType The DeleteType.
     */
    override fun deleteProduct(product: Product, deleteType: DeleteType) {
        disposables.add(deleteProduct.invoke(product)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, { })
        )
    }
}