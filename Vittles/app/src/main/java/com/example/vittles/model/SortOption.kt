package com.example.vittles.model

data class SortOption (var name: String) {

    companion object {

        val OPTION_NAMES = arrayOf(
            "Days Remaining (Low to High)",
            "Days Remaining (High to Low",
            "Alfabetic (A-Z)",
            "Alfabetic (Z-A)",
            "Newest",
            "Oldest"
        )
    }
}