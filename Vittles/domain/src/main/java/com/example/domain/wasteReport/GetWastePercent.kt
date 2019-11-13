package com.example.domain.wasteReport

import com.example.domain.repositories.WasteReportRepository
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject
import kotlin.math.ceil

/**
 * This class handles te business logic of getting waste report.
 *
 * @author Sarah Lange
 *
 * @property repository The WasteReportRepository.
 */
class GetWastePercent @Inject constructor(/*private val repository: WasteReportRepository*/) {

    /**
     * This method is used to get the percent value of eaten vittles
     *
     * @param date From this date up to now the amount is calculated
     * @return percent value of eaten vittles
     */
    operator fun invoke(vittlesEaten: Int, vittlesExpired: Int): Single<Int> {
        var percent = 0
        if((vittlesEaten + vittlesExpired) != 0 ) {
            percent =
                ceil((vittlesEaten.toDouble() / (vittlesEaten + vittlesExpired)) * 100).toInt()
        }
        return Single.just(percent)
    }

}