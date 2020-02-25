package com.example.vittles.termsandconditions

import com.example.domain.termsandconditions.TermsAndConditions

interface TermsAndConditionsContract {

    interface View {

        fun showTermsAndConditions(termsAndConditions: TermsAndConditions)
    }

    interface Presenter {

        fun onInitialize()
    }
}