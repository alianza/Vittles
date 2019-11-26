package com.example.vittles.productlist

import com.example.domain.product.Product

/**
 * Maps between the parcelable product in the app layer and the product in the domain
 *
 * @author Arjen Simons
 */
class ParcelableProductMapper {

    companion object {
        /**
         * Maps Parcelable product to Product in domain layer
         *
         * @param from The ParcelableProduct
         */
        fun fromParcelable(from: ParcelableProduct) = Product(
            from.uid,
            from.productName,
            from.expirationDate,
            from.creationDate,
            null
        )

        /**
         * Maps Product to ParcelableProduct
         *
         * @param from The Product in the domain layer
         */
        fun toParcelable(from: Product) = ParcelableProduct(
            from.uid,
            from.productName,
            from.expirationDate,
            from.creationDate
        )
    }
}