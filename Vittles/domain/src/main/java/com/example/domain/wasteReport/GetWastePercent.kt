package com.example.domain.wasteReport

import com.example.domain.repositories.WasteReportRepository
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * This class handles te business logic of getting waste report.
 *
 * @author Sarah Lange
 *
 * @property repository The WasteReportRepository.
 */
class GetWastePercent @Inject constructor(private val repository: WasteReportRepository) {


    operator fun invoke(date: DateTime): Single<Int> = repository.getPercent(date.millis)

}