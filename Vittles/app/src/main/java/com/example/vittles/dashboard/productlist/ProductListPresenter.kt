package com.example.vittles.dashboard.productlist

import com.example.domain.product.AddProduct
import com.example.domain.product.DeleteProduct
import com.example.domain.product.GetProducts
import com.example.domain.settings.GetVibrationEnabled
import com.example.domain.wasteReport.AddWasteReportProduct
import com.example.domain.wasteReport.WasteReportProduct
import com.example.vittles.enums.DeleteType
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import com.example.vittles.extension.*
import com.example.vittles.dashboard.ProductMapper
import com.example.vittles.dashboard.model.ProductViewModel
import javax.inject.Inject

/**
 * The presenter for the main product activity.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 * @author Sarah Lange
 *
 * @property getProducts The GetProducts use case from the domain module.
 * @property onProductDelete The DeleteProduct use cane from the domain module.
 * @property getVibrationEnabled The GetVibrationEnabled use case from the domain module
 * @property addWasteReportProduct The AddWasteReportProduct use case from the domain module.
 */
class ProductListPresenter @Inject internal constructor(
    private val mapper: ProductMapper,
    private val addProduct: AddProduct,
    private val getProducts: GetProducts,
    private val deleteProduct: DeleteProduct,
    private val addWasteReportProduct: AddWasteReportProduct,
    private val getVibrationEnabled: GetVibrationEnabled
) :
    BasePresenter<ProductListFragment>(), ProductListContract.Presenter {


    override fun start(view: ProductListFragment) {
        super.start(view)

        getProducts()
            .map { it.map(mapper::toParcelable) }
            .subscribeOnIoObserveOnMain()
            .subscribe({ view.onProductsUpdated(it) }, { TODO() })
            .addTo(disposables)
    }
    /**
     * Loads the products.
     *
     */
    override fun startPresenting() {

    }

    /**
     * Deletes a product.
     *
     * @param product The product that will be deleted.
     * @param deleteType The Delete Type, EATEN, THROWN_AWAY or REMOVED
     */
    override fun onProductDelete(product: ProductViewModel, deleteType: DeleteType) {
//        disposables.add(
//            deleteProduct.invoke(product)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ view?.setAllNoResultStates() }, { view?.onShowProductDeleteError() })
//        )
//        addWasteReportProduct(deleteType)
        deleteProduct(mapper.fromParcelable(product))
            .subscribeOnIoObserveOnMain()
            .subscribe()
            .addTo(disposables)
    }

    override fun onProductInsert(product: ProductViewModel) {
        addProduct(mapper.fromParcelable(product), false)
            .subscribeOnIoObserveOnMain()
            .subscribe()
            .addTo(disposables)
    }

    /**
     * Adds a waste report product to database when a product is deleted
     *
     * @param deleteType The delete type of the deleted product
     */
    private fun addWasteReportProduct(deleteType: DeleteType) {
        disposables.add(
            addWasteReportProduct.invoke(
                WasteReportProduct(
                    null,
                    DateTime.now().withTimeAtStartOfDay(),
                    deleteType.name
                )
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    /**
     * @return the boolean value of vibration
     */
    fun getVibrationSetting(): Boolean {
        return getVibrationEnabled()
    }
}