package com.example.data

import com.example.domain.repositories.WasteReportRepository
import com.example.domain.wasteReport.WasteReportProduct
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime

/**
 * This is the implementation of the WasteReportRepository in the Domain layer.
 *
 * @author Sarah Lange
 *
 * @property productDao Reference to the ProductDao.
 * @property mapper The mapper used to map the wasteReportProduct data class.
 */
class WasteReportRepositoryImpl(private val productDao: ProductDao,
                             private val mapper: WasteReportModelMapper) :
    WasteReportRepository {
    override fun post(wasteReportProduct: WasteReportProduct): Completable = Completable.fromAction { productDao
        .insertWasteReportProduct(mapper.toEntity(wasteReportProduct))
    }

    override fun getCountEatenProducts(date: Long): Single<Int> {
        return productDao.getCountEatenProducts(date)

    }

    override fun getCountExpiredProducts(date: Long): Single<Int> {
        return productDao.getCountExpiredProducts(date)
    }

    override fun getPercent(date: Long):Single<Int> {
        println("TESEEEEEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFT"+ productDao.getWastePercent(date))
        return productDao.getWastePercent(date)
    }

}