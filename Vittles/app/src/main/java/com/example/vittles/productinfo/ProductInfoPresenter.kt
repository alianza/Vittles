package com.example.vittles.productinfo

import com.example.domain.product.DeleteProduct
import com.example.domain.product.Product
import com.example.vittles.enums.DeleteType
import com.example.vittles.mvp.BasePresenter
import javax.inject.Inject

class ProductInfoPresenter @Inject internal constructor(
    private val deleteProduct: DeleteProduct
) : BasePresenter<ProductInfoFragment>(), ProductInfoContract.Presenter {

    override fun updateProduct(product: Product) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteProduct(product: Product, deleteType: DeleteType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}