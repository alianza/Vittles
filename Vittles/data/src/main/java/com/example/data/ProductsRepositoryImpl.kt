package com.example.data

import com.example.data.retrofit.off.OffApiService
import com.example.data.retrofit.tsco.TscoApiService
import com.example.data.room.ProductDao
import com.example.data.room.ProductModelMapper
import com.example.domain.exceptions.ProductNotFoundException
import com.example.domain.repositories.ProductsRepository
import com.example.domain.product.Product
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * This is the implementation of the ProductsRepository in the Domain layer.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property productDao Reference to the ProductDao.
 * @property productsApiTSCO Reference to the TscoApiService.
 * @property productsApiOFF Reference to the OffApiService.
 * @property mapper The mapper used to map the product data class.
 */
class ProductsRepositoryImpl(private val productDao: ProductDao,
                             private val productsApiTSCO: TscoApiService,
                             private val productsApiOFF: OffApiService,
                             private val mapper: ProductModelMapper
) :
    ProductsRepository {

    /** {@inheritDoc} */
    override fun get(): Single<List<Product>> {
        return productDao.getAll()
            .map { it.map(mapper::fromEntity) }
    }

    /** {@inheritDoc} */
    override fun patch(product: Product): Completable = Completable.fromAction { productDao.insert(mapper.toEntity(product)) }

    /** {@inheritDoc} */
    override fun delete(product: Product): Completable = Completable.fromAction { productDao.delete(mapper.toEntity(product)) }

    /** {@inheritDoc} */
    override fun post(product: Product): Completable = Completable.fromAction { productDao.insert(mapper.toEntity(product)) }

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeTSCO(barcode: String): Observable<String> {
        return productsApiTSCO.getProductName(barcode).map {
            if (it.products?.size!! > 0) {
                it.products?.get(0)?.value
            } else {
                throw ProductNotFoundException(barcode)
            }
        }
    }

    /** {@inheritDoc} */
    override fun getProductNameByBarcodeOFF(barcode: String): Observable<String> {
        return productsApiOFF.getProductName(barcode).map {
            if (it.status == 1) {
                it.product?.productName
            } else {
                throw ProductNotFoundException(barcode)
            }
        }
    }
}