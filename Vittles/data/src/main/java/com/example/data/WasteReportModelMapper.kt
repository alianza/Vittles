package com.example.data

import com.example.domain.wasteReport.WasteReportProduct
import javax.inject.Inject

/**
 * Maps between Room database entity and model.
 *
 * @author Sarah Lange
 */
class WasteReportModelMapper @Inject constructor() {
    fun fromEntity(from: WasteReportEntity) = WasteReportProduct(from.uid, from.creationDate, from.wasteType)
    fun toEntity(from: WasteReportProduct) = WasteReportEntity(from.uid, from.creationDate, from.wasteType)
}