package com.example.vittles.scanning

import android.content.pm.PackageManager
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.domain.barcode.AddProductDictionary
import com.example.domain.barcode.GetProductByBarcode
import com.example.domain.barcode.ProductDictionary
import com.example.domain.barcode.UpdateProductDictionary
import com.example.domain.product.AddProduct
import com.example.domain.product.model.Product
import com.example.domain.settings.GetPerformanceSetting
import com.example.domain.settings.GetVibrationEnabled
import com.example.domain.settings.SetPerformanceSetting
import com.example.domain.settings.model.PerformanceSetting
import com.example.vittles.app.VittlesApp.PermissionProperties.REQUIRED_PERMISSIONS
import com.example.vittles.mvp.BasePresenter
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_scanner.*
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * @author Jeroen Flietstra
 */
class ScannerPresenter @Inject internal constructor(
    private val getProductByBarcode: GetProductByBarcode,
    private val addProduct: AddProduct,
    private val addProductDictionary: AddProductDictionary,
    private val updateProductDictionary: UpdateProductDictionary,
    private val getVibrationEnabled: GetVibrationEnabled,
    private val getPerformanceSetting: GetPerformanceSetting,
    private val setPerformanceSetting: SetPerformanceSetting
    ) :
    BasePresenter<ScannerFragment>(), ScannerContract.Presenter {

    /** CameraX preview element. */
    private lateinit var preview: Preview
    /** ImageAnalysis object with the PreviewAnalyzer in it for the preview analysis. */
    private lateinit var imageAnalysis: ImageAnalysis
    /** Executor for the analysis on a different thread. */
    private val executor = Executors.newSingleThreadExecutor()
    /** The analyzer for the preview */
    private lateinit var analyzer: PreviewAnalyzer
    /** observes LiveData objects for changes.**/
    private val performanceSetting = MutableLiveData<PerformanceSetting>()

    override fun addProductToList(product: Product, checkDate: Boolean) {
        disposables.add(addProduct(product, checkDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view?.onShowAddProductSucceed()
                    view?.onResetView()
                },
                {
                    if (it is IllegalArgumentException) {
                        view?.onShowAddProductError() // Show snack bar that tells it failed
                    } else if (it is Exception) {
                        view?.onShowExpirationPopup(product) // Show close to expiring popup
                    }
                }
            )
        )
    }

    override fun insertProductDictionary(productDictionary: ProductDictionary) {
        if (!productDictionary.containsNotReady()) {
            disposables.add(addProductDictionary(productDictionary)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    override fun patchProductDictionary(productDictionary: ProductDictionary) {
        disposables.add(updateProductDictionary(productDictionary)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    override fun getVibrationSetting(): Boolean {
        return getVibrationEnabled()
    }

    override fun startCamera() {
        imageAnalysis = getImageAnalysis()
        preview = getPreview()

        CameraX.bindToLifecycle(view, preview, imageAnalysis)

        disableTorch()
    }

    /**
     * Creates a CameraX preview object.
     *
     * @return The CameraX preview.
     */
    override fun getPreview(): Preview {
        // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.BACK)
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
        }.build()

        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {
            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = view?.textureView?.parent as ViewGroup
            val textureView = view?.textureView
            parent.removeView(view?.textureView)
            parent.addView(textureView, 0)

            view?.textureView?.surfaceTexture = it.surfaceTexture
        }
        return preview
    }

    /**
     * Creates an ImageAnalysis used for the preview analysis.
     *
     * @return The CameraX ImageAnalysis.
     */
    override fun getImageAnalysis(): ImageAnalysis {
        // Setup image analysis pipeline that computes average pixel luminance
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            setTargetResolution(Size(720, 480)) // 480p resolution
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE) // Take newest available frame on analyze call
        }.build()

        PreviewAnalyzer.hasBarCode = false
        PreviewAnalyzer.hasExpirationDate = false

        fetchPerformanceSetting()

        // Build the image analysis use case and instantiate our analyzer
        return ImageAnalysis(analyzerConfig).apply {
            setAnalyzer(executor, PreviewAnalyzer(
                onBarcodeFailure = { view?.onBarcodeNotFound() },
                onBarcodeSuccess = { getProductNameByBarcode(it) },
                onOcrFailure = { view?.onTextNotFound() },
                onOcrSuccess = { view?.onTextScanned(it) },
                onOutOfMemory = { view?.onAnalyzerError() },
                performanceSetting = performanceSetting.value!!
            ))
        }
    }

    private fun fetchPerformanceSetting() {
        performanceSetting.value = getPerformanceSetting()
    }

    override fun checkPermissions() {
        // Request camera permissions
        if (allPermissionsGranted()) {
            view?.textureView?.post { startCamera() }
        } else {
            view?.let {
                view?.onRequestPermissionsFromFragment()
            }
        }
    }

    override fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        view?.context.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1!!, it
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onPerformanceSettingLowered() {
        val currentPerformanceSetting = getPerformanceSetting().ordinal
        if (currentPerformanceSetting > 0) {
            val newPerformanceSetting = PerformanceSetting.values()[currentPerformanceSetting - 1]
            setPerformanceSetting(newPerformanceSetting)
        }
    }

    /**
     * Calls repository to retrieve the product name with the given barcodes.
     *
     * @param barcodes All the barcodes retrieved from the camera.
     */
    private fun getProductNameByBarcode(barcodes: List<FirebaseVisionBarcode>) {
        if (barcodes.isNotEmpty()) {
            val barcode = barcodes[0].rawValue.toString()
            disposables.add(
                getProductByBarcode(barcode).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.onBarcodeScanned(it) }, { view?.onBarcodeNotFound() })
            )
        }
    }

    fun toggleTorch(): Boolean {
        return if (preview.isTorchOn) {
            disableTorch()
            false
        } else {
            enableTorch()
            true
        }
    }

    fun torchEnabled(): Boolean {
        return if (::preview.isInitialized) {
            preview.isTorchOn
        } else {
            false
        }
    }

    private fun enableTorch() {
        if (::preview.isInitialized) {
            preview.enableTorch(true)
        }
    }

    private fun disableTorch() {
        if (::preview.isInitialized) {
            preview.enableTorch(false)
        }
    }
}