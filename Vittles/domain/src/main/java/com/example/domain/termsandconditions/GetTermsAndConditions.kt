package com.example.domain.termsandconditions

import io.reactivex.Observable
import javax.inject.Inject

class GetTermsAndConditions @Inject constructor(
    private val repository: TermsAndConditionsRepository
) {

    operator fun invoke(): Observable<TermsAndConditions> = repository.getTermsAndConditions()
}