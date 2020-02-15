package com.example.vittles.productlist

import com.example.domain.product.Product
import javax.inject.Inject

class ProductMapper @Inject constructor() {

    fun fromParcelable(from: ProductViewModel) = Product(
        from.uid,
        from.productName,
        from.expirationDate,
        from.creationDate
    )
    
    fun toParcelable(from: Product) = ProductViewModel(
        from.uid,
        from.productName,
        from.expirationDate,
        from.creationDate,
        from.getDaysRemaining(),
        null
    )
}