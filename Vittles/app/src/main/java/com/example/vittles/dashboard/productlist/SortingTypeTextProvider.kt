package com.example.vittles.dashboard.productlist

import android.content.Context
import com.example.vittles.R
import com.example.domain.product.model.ProductSortingType
import javax.inject.Inject

class SortingTypeTextProvider @Inject internal constructor(
    private val context: Context
) {

    fun getSortingTypeText(sortingType: ProductSortingType): String {
        return when (sortingType) {
            ProductSortingType.DAYS_REMAINING_ASC -> context.getString(R.string.days_remaining_lh)
            ProductSortingType.DAYS_REMAINING_DESC -> context.getString(R.string.days_remaining_hl)
            ProductSortingType.ALPHABETIC_ASC -> context.getString(R.string.alphabetic_az)
            ProductSortingType.ALPHABETIC_DESC -> context.getString(R.string.alphabetic_za)
            ProductSortingType.CREATION_DATE_DESC -> context.getString(R.string.newest)
            ProductSortingType.CREATION_DATE_ASC -> context.getString(R.string.oldest)
        }

    }
}