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
    /*operator fun invoke(date: DateTime): Single<BarChartData> {

        return repository.getWasteReportProducts(date.millis).map { products ->
            val listEaten = mutableListOf<Int>()
            val listExpired = mutableListOf<Int>()

            for (i in 0..6) {
                val productAmount = products.count {
                    it.creationDate == DateTime.now().minusDays(i).withTimeAtStartOfDay()
                }
                val expiring =
                    products.count {
                        it.wasteType == "EATEN" && it.creationDate ==
                                DateTime.now().minusDays(i).withTimeAtStartOfDay()
                    }

                val percentEaten: Float
                val percentExpired: Float
                if (productAmount != 0) {
                    percentEaten = expiring.toFloat() / productAmount.toFloat() * 100
                    percentExpired =
                        (products.size - expiring.toFloat()) / productAmount.toFloat() * 100
                } else {
                    percentEaten = 0f
                    percentExpired = 0f
                }
                listEaten.add(percentEaten.toInt())
                listExpired.add(percentExpired.toInt())
            }
            BarChartData(listEaten, listExpired)
        }
    }*/

    operator fun invoke(date: DateTime): Single<List<BarChartEntry>> {

        return repository.getWasteReportProducts(date.millis).map { products ->
            val listBarChartEntry = mutableListOf<BarChartEntry>()

            for (i in 0..6) {
                val loopDate = DateTime.now().minusDays(i).withTimeAtStartOfDay()
                val productAmount = products.count {
                    it.creationDate == loopDate
                }
                val expiring =
                    products.count {
                        it.wasteType == "EATEN" && it.creationDate == loopDate
                    }

                val percentEaten: Float
                val percentExpired: Float
                if (productAmount != 0) {
                    percentEaten = expiring.toFloat() / productAmount.toFloat() * 100
                    percentExpired =
                        (productAmount - expiring.toFloat()) / productAmount.toFloat() * 100
                } else {
                    percentEaten = 0f
                    percentExpired = 0f
                }
                listBarChartEntry.add(BarChartEntry(percentEaten, percentExpired, loopDate))
            }
            listBarChartEntry
        }
    }

}