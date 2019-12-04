package com.example.vittles.settings

import com.example.domain.barcode.EmptyProductDictionary
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The presenter for the settings activity.
 *
 * @author Jan-Willem van BRemen
 *
 * @param emptyProductDictionary The EmptyProductDictionary use case from the domain module.
 */

class SettingsPresenter @Inject internal constructor(
    private val emptyProductDictionary: EmptyProductDictionary
) :
    BasePresenter<SettingsFragment>(), SettingsContract.Presenter {

    /**
     * Method used to remove all local product names
     *
     */
    override fun clearProductDictionary() {
        disposables.add(
            emptyProductDictionary().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.onProductDictionaryClearSuccess() }, { view?.onProductDictionaryClearFail() })
        )
    }
}