package com.example.vittles

data class Product(
    val name: String,
    val expirationDate: String,
    val daysUntilExpiration: Int
) { //TODO: Delete dummy data if we have real data
    companion object {
        val PRODUCT_NAMES = arrayOf(
            "Milk",
            "Yoghurt",
            "Chicken"
        )

        val PRODUCT_EXPIRATION_DATES = arrayOf(
            "3-10-2019",
            "10-11-2019",
            "21-10-2019"
        )

        val PRODUCTS_DAYS_UNTIL_EXPIRATION = arrayOf(
            10,
            39,
            17
        )
    }
}
