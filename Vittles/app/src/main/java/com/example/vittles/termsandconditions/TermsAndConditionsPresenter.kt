package com.example.vittles.termsandconditions

import com.example.domain.termsandconditions.GetTermsAndConditions
import com.example.vittles.extension.addTo
import com.example.vittles.extension.subscribeOnIoObserveOnMain
import com.example.vittles.mvp.BasePresenter
import javax.inject.Inject

class TermsAndConditionsPresenter @Inject constructor(
    private val getTermsAndConditions: GetTermsAndConditions
) : BasePresenter<TermsAndConditionsActivity>(), TermsAndConditionsContract.Presenter {

    override fun onInitialize() {
        getTermsAndConditions()
            .subscribeOnIoObserveOnMain()
            .subscribe( { view?.showTermsAndConditions(it) }, { it.printStackTrace() })
            .addTo(disposables)
    }
}