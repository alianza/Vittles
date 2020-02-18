package com.example.vittles.dashboard.productlist

import android.content.Context
import com.example.vittles.R
import com.example.vittles.dashboard.model.ProductSortingType
import javax.inject.Inject

class SortingTypeTextProvider @Inject internal constructor(
    private val context: Context
) {

    fun getSortingTypeText(sortingType: ProductSortingType): String {
        return when (sortingType) {
            ProductSortingType.DAYS_REMAINING_ASC -> context.getString(R.string.days_remaining_lh)
            ProductSortingType.DAYS_REMAINING_DESC -> context.getString(R.string.days_remaining_hl)
            ProductSortingType.ALPHABETIC_AZ -> context.getString(R.string.alphabetic_az)
            ProductSortingType.ALPHABETIC_ZA -> context.getString(R.string.alphabetic_za)
            ProductSortingType.NEWEST -> context.getString(R.string.newest)
            ProductSortingType.OLDEST -> context.getString(R.string.oldest)
        }

    }
}