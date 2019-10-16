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
 * @property onProductCloseToExpiring Event that is fired when an added product is close to expiring.
 */
class AddProductUseCase @Inject constructor(private val repository: ProductsRepository) {

    val onProductCloseToExpiring = Event<Int>()

    /**
     * This method is used to add a product to the database.
     *
     * @param product The product that will be added.
     * @return The compatibility status of adding the product ot the database.
     */
    fun add(product: Product): Completable = validate(product).andThen(repository.post(product))

    /**
     * Validates if the product can be added to the database.
     *
     * @param product The product that will be added to the database.
     * @return The compatibility status.
     */
    private fun validate(product: Product): Completable {

        checkExpirationDate(product.getDaysRemaining())

        return if (!product.isValidForAdd()) {
            Completable.error(IllegalArgumentException("product failed validation before add"))
        } else {
            Completable.complete()
        }
    }

    /**
     * Checks the expiration date and invokes the onProductCloseToExpiring when it is close to expiring.
     *
     * @param daysRemaining The amount of days until expiring.
     */
    private fun checkExpirationDate(daysRemaining: Int){
        if (daysRemaining <= DAYS_REMAINING_BOUNDARY){
            onProductCloseToExpiring.invoke(daysRemaining)
        }
    }
}