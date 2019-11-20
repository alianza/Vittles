package com.example.vittles.scanning

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import com.example.domain.barcode.GetProductByBarcode
import com.example.domain.product.AddProduct
import com.example.domain.product.Product
import com.example.vittles.mvp.BasePresenter
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_camera.*
import java.lang.Exception
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * The presenter for the scanning activity.
 *
 * @property getProductByBarcode The GetProductByBarcode use case from the domain module.
 */
class ScannerPresenter @Inject internal constructor(
    private val getProductByBarcode: GetProductByBarcode,
    private val addProduct: AddProduct
) :
    BasePresenter<ScannerFragment>(), ScannerContract.Presenter {

    // CameraX preview element
    private lateinit var preview: Preview
    // ImageAnalysis object with the PreviewAnalyzer in it for the preview analysis
    private lateinit var imageAnalysis: ImageAnalysis
    // Executor for the analysis on a different thread
    private val executor = Executors.newSingleThreadExecutor()
    // The analyzer for the preview
    private lateinit var analyzer: PreviewAnalyzer

    /**
     * Method used to add a product.
     *
     * @param product The product to add.
     * @param checkDate If the date should be checked to show a popup.
     */
    fun addProduct(product: Product, checkDate: Boolean) {
        disposables.add(addProduct.invoke(product, checkDate)
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

    /**
     * Sets up everything for the camera to start.
     *
     */
    override fun startCamera() {
        imageAnalysis = getImageAnalysis()
        preview = getPreview()

        CameraX.bindToLifecycle(view, preview, imageAnalysis)
        analyzer = imageAnalysis.analyzer as PreviewAnalyzer
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
     * @return The CameraX ImageAnalysis
     */
    override fun getImageAnalysis(): ImageAnalysis {
        // Setup image analysis pipeline that computes average pixel luminance
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            setTargetResolution(Size(720, 480)) // 480p resolution
            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE) // Take newest available frame on analyze call
        }.build()

        PreviewAnalyzer.hasBarCode = false
        PreviewAnalyzer.hasExpirationDate = false

        // Build the image analysis use case and instantiate our analyzer
        return ImageAnalysis(analyzerConfig).apply {
            setAnalyzer(executor, PreviewAnalyzer(
                onBarcodeFailure = { view?.onBarcodeNotFound() },
                onBarcodeSuccess = { getProductNameByBarcode(it) },
                onOcrFailure = { view?.onTextNotFound() },
                onOcrSuccess = { view?.onTextScanned(it) }
            ))
        }
    }

    /**
     * Checks if the user gave all needed permissions for the activity to work properly.
     *
     */
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

    /**
     * Check if all permission specified in the manifest have been granted.
     */
    override fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        view?.context.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1!!, it
            )
        } == PackageManager.PERMISSION_GRANTED
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

    /**
     * Toggles the flash (torch) of the camera.
     *
     * @return Boolean value that represents if torch is on or off.
     */
    fun toggleTorch(): Boolean {
        return if (preview.isTorchOn) {
            preview.enableTorch(false)
            false
        } else {
            preview.enableTorch(true)
            true
        }
    }

    companion object {
        /*
        This is an arbitrary number we are using to keep track of the permission
        request. Where an app has multiple context for requesting permission,
        this can help differentiate the different contexts.
        */
        const val REQUEST_CODE_PERMISSIONS = 10

        // This is an array of all the permission specified in the manifest.
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}