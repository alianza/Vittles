package com.example.vittles.camera

import com.example.domain.barcode.GetProductByBarcode
import com.example.vittles.mvp.BasePresenter
import javax.inject.Inject

class CameraPresenter @Inject internal constructor(private val getProductByBarcode: GetProductByBarcode) : BasePresenter<CameraActivity>() {

}