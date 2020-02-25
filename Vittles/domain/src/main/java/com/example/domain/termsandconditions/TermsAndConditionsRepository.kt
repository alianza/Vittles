package com.example.domain.termsandconditions

import io.reactivex.Observable

interface TermsAndConditionsRepository {

    fun getTermsAndConditions(): Observable<TermsAndConditions>
}