package com.example.vittles.settings

import com.example.domain.barcode.EmptyProductDictionary
import com.example.vittles.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsPresenter @Inject internal constructor(
    private val emptyProductDictionary: EmptyProductDictionary
) :
    BasePresenter<SettingsFragment>(), SettingsContract.Presenter {

    override fun clearProductDictionary() {
        disposables.add(
            emptyProductDictionary().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.onProductDictionaryClearSuccess() }, { view?.onProductDictionaryClearFail() })
        )
    }
}