package com.example.data

import com.example.data.retrofit.off.OffApiService
import com.example.data.retrofit.tsco.TscoApiService
import com.example.data.room.barcodedictionary.BarcodeDao
import com.example.data.room.barcodedictionary.BarcodeDictionaryModelMapper
import com.example.domain.barcode.BarcodeDictionary
import com.example.domain.exceptions.ProductNotFoundException
import com.example.domain.repositories.BarcodesRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * This is the implementation of the BarcodesRepository in the Domain layer.
 *
 * @author Jeroen Flietstra
 *
 * @property barcodeDao Reference to the BarcodeDao.
 * @property productsApiTSCO Reference to the TscoApiService.
 * @property productsApiOFF Reference to the OffApiService.
 * @property mapper The mapper used to map the barcode dictionary data class.
 */
class BarcodesRepositoryImpl(
    private val barcodeDao: BarcodeDao,
    private val productsApiTSCO: TscoApiService,
    private val productsApiOFF: OffApiService,
    private val mapper: BarcodeDictionaryModelMapper
) : BarcodesRepository {

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeTSCO(barcode: String): Observable<BarcodeDictionary> {
        return productsApiTSCO.getProductName(barcode).map {
            if (it.products?.size!! > 0) {
                BarcodeDictionary(barcode, it.products?.get(0)?.value)
            } else {
                throw ProductNotFoundException(barcode)
            }
        }
    }

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeOFF(barcode: String): Observable<BarcodeDictionary> {
        return productsApiOFF.getProductName(barcode).map {
            if (it.status == 1) {
                BarcodeDictionary(barcode, it.product?.productName)
            } else {
                throw ProductNotFoundException(barcode)
            }
        }
    }

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeRoom(barcode: String): Observable<BarcodeDictionary> =
        barcodeDao.getBarcode(barcode).map {
            mapper.fromEntity(it)
        }.doOnComplete {
            throw ProductNotFoundException(barcode)
        }.toObservable()

    /** {@inheritDoc} */
    override fun insertBarcodeDictionaryRoom(barcodeDictionary: BarcodeDictionary): Completable =
        Completable.fromAction { barcodeDao.insertBarcode(mapper.toEntity(barcodeDictionary)) }

    /** {@inheritDoc} */
    override fun updateBarcodeDictionaryRoom(barcodeDictionary: BarcodeDictionary): Completable =
        Completable.fromAction { barcodeDao.updateBarcode(mapper.toEntity(barcodeDictionary)) }
}