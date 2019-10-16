package com.example.domain.productadd

import android.app.usage.UsageEvents
import android.util.EventLog
import com.example.domain.Event.Event
import com.example.domain.consts.DAYS_REMAINING_BOUNDARY
import com.example.domain.repositories.ProductsRepository
import com.example.domain.model.Product
import io.reactivex.Completable
import javax.inject.Inject

/**
 * This class handles te business logic of adding a new product to the application.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property repository The productsRepository.
 */
class AddProductUseCase @Inject constructor(private val repository: ProductsRepository) {

    /**
     * This method is used to add a product to the database.
     *
     * @param product The product that will be added.
     * @param checkDate If the date should be checked to show a product
     * @return The compatibility status of adding the product ot the database.
     */
    fun add(product: Product, checkDate: Boolean): Completable = validate(product, checkDate).andThen(repository.post(product))

    /**
     * Validates if the product can be added to the database.
     *
     * @param product The product that will be added to the database.
     * @param checkDate If the date should be checked to show a popup.
     * @return The compatibility status.
     */
    private fun validate(product: Product, checkDate: Boolean): Completable {
        return if (!product.isValidForAdd()) {
            Completable.error(IllegalArgumentException("product failed validation before add"))
        }else if(checkDate && productIsCloseToExpiring(product.getDaysRemaining())) {
            Completable.error(Exception("Product to close to expiring, popup should show"))
        } else {
            Completable.complete()
        }
    }

    /**
     * Checks the expiration date and invokes the onProductCloseToExpiring when it is close to expiring.
     *
     * @param daysRemaining The amount of days until expiring.
     */
    private fun productIsCloseToExpiring(daysRemaining: Int): Boolean{
        return daysRemaining <= DAYS_REMAINING_BOUNDARY
    }
}