package com.example.data

import com.example.data.retrofit.off.OffApiService
import com.example.data.retrofit.tsco.TscoApiService
import com.example.data.room.barcodedictionary.BarcodeDao
import com.example.data.room.barcodedictionary.BarcodeDictionaryModelMapper
import com.example.domain.barcode.ProductDictionary
import com.example.domain.exceptions.ProductNotFoundException
import com.example.domain.repositories.BarcodesRepository
import io.reactivex.Completable
import io.reactivex.Observable

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
    override fun getProductNameByBarcodeTSCO(barcode: String): Observable<ProductDictionary> {
        return productsApiTSCO.getProductName(barcode).map {
            if (it.products?.size!! > 0) {
                ProductDictionary(barcode, it.products?.get(0)?.value)
            } else {
                throw ProductNotFoundException(barcode)
            }
        }
    }

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeOFF(barcode: String): Observable<ProductDictionary> {
        return productsApiOFF.getProductName(barcode).map {
            if (it.status == 1) {
                ProductDictionary(barcode, it.product?.productName)
            } else {
                throw ProductNotFoundException(barcode)
            }
        }
    }

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeRoom(barcode: String): Observable<ProductDictionary> =
        barcodeDao.getBarcode(barcode).map {
            mapper.fromEntity(it)
        }.doOnComplete {
            throw ProductNotFoundException(barcode)
        }.toObservable()

    /** {@inheritDoc} */
    override fun insertBarcodeDictionaryRoom(productDictionary: ProductDictionary): Completable =
        Completable.fromAction { barcodeDao.insertBarcode(mapper.toEntity(productDictionary)) }

    /** {@inheritDoc} */
    override fun updateBarcodeDictionaryRoom(productDictionary: ProductDictionary): Completable =
        Completable.fromAction { barcodeDao.updateBarcode(mapper.toEntity(productDictionary)) }
}