package com.example.domain.repositories

import com.example.domain.wasteReport.WasteReportProduct
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Repository interface for the wasteReportProducts witch is implemented in the data module.
 *
 * @author Sarah Lange
 */
interface WasteReportRepository {

    /**
     * Adds a wasteReportProduct in the database.
     *
     * @param wasteReportProduct The product to invoke.
     * @return A completable status.
     */
    fun post(wasteReportProduct: WasteReportProduct): Completable

    /**
     * Gets amount of eaten vittles
     *
     * @param date From this date up to now the amount is calculated
     * @return amount of eaten vittles
     */
    fun getCountEatenProducts(date: Long): Single<Int>

    /**
     * Gets amount of expired vittles
     *
     * @param date From this date up to now the amount is calculated
     * @return amount of expired vittles
     */
    fun getCountExpiredProducts(date: Long): Single<Int>

    /**
     * This method is used to get the percent value of eaten vittles
     *
     * @param date from this date up to now the amount is calculated
     * @return percent value of eaten vittles
     */
    fun getEatenPercent(date: Long): Single<Int>
}