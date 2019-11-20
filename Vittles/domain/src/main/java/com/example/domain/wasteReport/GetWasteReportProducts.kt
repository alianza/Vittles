package com.example.domain.wasteReport

import com.example.domain.repositories.WasteReportRepository
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * This class handles te business logic of getting waste report products
 *
 * @author Sarah Lange
 *
 * @property repository The WasteReportRepository.
 */
class GetWasteReportProducts @Inject constructor(private val repository: WasteReportRepository) {

    /**
     * Gets the waste report products
     *
     * @param date From this date up to now the data should be given
     * @return List of vittles
     */
    operator fun invoke(date: DateTime): Single<List<WasteReportProduct>> = repository.getWasteReportProducts(date.millis)


}