package com.example.vittles.dashboard.productlist

import com.example.domain.product.AddProduct
import com.example.domain.product.DeleteProduct
import com.example.domain.product.GetProductsSortedWithQuery
import com.example.domain.product.model.ProductSortingType
import com.example.domain.settings.GetVibrationEnabled
import com.example.domain.wastereport.AddWasteReportProduct
import com.example.domain.wastereport.WasteReportProduct
import com.example.vittles.dashboard.ProductMapper
import com.example.vittles.dashboard.model.ProductViewModel
import com.example.vittles.enums.DeleteType
import com.example.vittles.extension.addTo
import com.example.vittles.extension.mapListItems
import com.example.vittles.extension.subscribeOnIoObserveOnMain
import com.example.vittles.main.MainActivity
import com.example.vittles.mvp.BasePresenter
import io.reactivex.disposables.Disposable
import org.joda.time.DateTime
import javax.inject.Inject

class ProductListPresenter @Inject internal constructor(
    private val mapper: ProductMapper,
    private val addProduct: AddProduct,
    private val getProducts: GetProductsSortedWithQuery,
    private val deleteProduct: DeleteProduct,
    private val addWasteReportProduct: AddWasteReportProduct,
    private val getVibrationEnabled: GetVibrationEnabled
) :
    BasePresenter<ProductListFragment>(), ProductListContract.Presenter {

    private var getProductsDisposable: Disposable? = null
        set(value) {
            getProductsDisposable?.dispose()
            field = value
        }

    override fun onListInitializeOrChange(sortingType: ProductSortingType, query: String) {
        getProductsDisposable = getProducts(sortingType, query)
            .mapListItems { mapper.toParcelable(it) }
            .subscribeOnIoObserveOnMain()
            .doOnSubscribe { view?.onShowLoadingView()}
            .subscribe({
                view?.onHideLoadingView()
                view?.onProductsUpdated(it)
            }, {
                val activity = view?.requireActivity() as MainActivity
                activity.createErrorToast()
            })
            .addTo(disposables)
    }

    override fun onProductDelete(product: ProductViewModel, deleteType: DeleteType) {
        deleteProduct(mapper.fromParcelable(product))
            .subscribeOnIoObserveOnMain()
            .doOnError {
                val activity = view?.requireActivity() as MainActivity
                activity.createErrorToast()
            }
            .subscribe()
            .addTo(disposables)
    }

    override fun onProductInsert(product: ProductViewModel) {
        addProduct(mapper.fromParcelable(product), false)
            .subscribeOnIoObserveOnMain()
            .subscribe()
            .addTo(disposables)
    }

    override fun onProductDeleted(deleteType: DeleteType) {
        disposables.add(
            addWasteReportProduct.invoke(
                WasteReportProduct(
                    null,
                    DateTime.now().withTimeAtStartOfDay(),
                    deleteType.name
                )
            )
                .subscribeOnIoObserveOnMain()
                .subscribe()
        )
    }

    fun getVibrationSetting(): Boolean {
        return getVibrationEnabled()
    }
}