package com.example.vittles.dashboard

import com.example.domain.product.model.Product
import com.example.vittles.dashboard.model.ProductViewModel
import javax.inject.Inject

class ProductMapper @Inject constructor() {

    fun fromParcelable(from: ProductViewModel) = Product(
        from.uid,
        from.productName,
        from.expirationDate,
        from.creationDate,
        from.barcode
    )
    
    fun toParcelable(from: Product) =
        ProductViewModel(
            from.uid,
            from.productName,
            from.expirationDate,
            from.creationDate,
            from.getDaysRemaining(),
            from.barcode,
            null
        )
}