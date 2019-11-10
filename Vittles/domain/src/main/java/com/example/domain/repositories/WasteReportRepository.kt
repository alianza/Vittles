package com.example.domain.repositories

import com.example.domain.product.Product
import com.example.domain.wasteReport.WasteReportProduct
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime


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

    fun getCountEatenProducts(date: Long): Single<Int>

    fun getCountExpiredProducts(date: Long): Single<Int>

    fun getPercent(date: Long): Single<Int>
}