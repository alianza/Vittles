package com.example.vittles.enums

import com.example.domain.enums.Invokable

enum class PreviousFragmentIndex(protected val index: Int) : Invokable {
    PRODUCT_LIST(0) {
        override operator fun invoke(): Int = index
    },
    WASTE_REPORT(1) {
        override operator fun invoke(): Int = index
    },
    SETTINGS(2) {
        override operator fun invoke(): Int = index
    }
}