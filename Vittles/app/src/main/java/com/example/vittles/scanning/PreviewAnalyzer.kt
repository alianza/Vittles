package com.example.vittles.scanning

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.domain.settings.model.PerformanceSetting
import com.example.vittles.services.scanner.ScanningService
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Analyzer used by CameraX to analyze frames from the camera preview.
 *
 * @author Jeroen Flietstra
 * @author Marc van Spronsen
 * @author Jan-Willem van Bremen
 *
 * @property onBarcodeSuccess Callback function for successful barcode scan.
 * @property onBarcodeFailure Callback function for unsuccessful barcode scan.
 * @property onOcrSuccess Callback function for successful OCR scan.
 * @property onOcrFailure Callback function for unsuccessful OCR scan.
 */
class PreviewAnalyzer(
    private val onBarcodeFailure: (exception: Exception) -> Unit,
    private val onBarcodeSuccess: (barcodes: List<FirebaseVisionBarcode>) -> Unit,
    private val onOcrFailure: (exception: Exception) -> Unit,
    private val onOcrSuccess: (text: String) -> Unit,
    private val onOutOfMemory: () -> Unit,
    private val performanceSetting: PerformanceSetting
) : ImageAnalysis.Analyzer {

    companion object {
        /** Value that indicates the minimum percentage of allocated heap left. */
        private const val MIN_ALLOCATED_HEAP_LEFT_PERCENTAGE = 5
        /** Value that indicates if a barcode has been scanned already */
        var hasBarCode = false
        /** Value that indicates if an expiration date has been scanned already */
        var hasExpirationDate = false
    }

    /** Value used for the scanning delay */
    private var lastAnalyzedTimestamp = 0L
    private var barcodeTask: Task<MutableList<FirebaseVisionBarcode>>? = null
    private var ocrTask: Task<FirebaseVisionText>? = null

    /**
     * Convert the rotation degree to firebase rotation degree.
     *
     * @param degrees The degrees value from CameraX.
     * @return Firebase rotation degree value.
     */
    private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    /**
     * {@link ImageAnalysis.Analyzer}
     *  Processes the frames of the camera.
     *
     * @param imageProxy Current frame of the camera.
     * @param degrees Rotation degree of the camera.
     */
    override fun analyze(imageProxy: ImageProxy?, degrees: Int) {
        if (!hasAvailableHeap()) {
            onOutOfMemory()
            return
        }

        // Scan only every {200, 500, 1000 ms} instead of every frame.
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >=
            TimeUnit.MILLISECONDS.toMillis(performanceSetting.ms)
        ) {
            val mediaImage = imageProxy?.image
            val imageRotation = degreesToFirebaseRotation(degrees)
            if (mediaImage != null) {
                var image: FirebaseVisionImage? = null
                try {
                    image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
                } catch (e: IllegalStateException) {
                    /*
                    NOTE: Non-fixable bug. Bug appears in fewer than 1% of all cases. At least this
                    try-catch might prevent the app from crashing.
                     */
                }
                if (!hasBarCode && image != null && barcodeTask?.isComplete != false) {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            barcodeTask = ScanningService.scanForBarcode(
                                image,
                                onBarcodeSuccess,
                                onBarcodeFailure
                            )
                        }
                    }
                }
                if (!hasExpirationDate && image != null && ocrTask?.isComplete != false) {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            ocrTask = ScanningService.scanForExpirationDate(
                                image,
                                onOcrSuccess,
                                onOcrFailure
                            )
                        }
                    }
                }
                lastAnalyzedTimestamp = currentTimestamp
            }
        }
    }

    /**
     * Checks if the app has enable memory allocated in order to use the camera.
     *
     * @return Boolean value that represents if the app has enough allocated memory.
     */
    private fun hasAvailableHeap(): Boolean {
        val runtime = Runtime.getRuntime()
        val usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L
        val maxHeapSizeInMB = runtime.maxMemory() / 1048576L
        val availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB
        val percentage = (availHeapSizeInMB.toDouble() / maxHeapSizeInMB.toDouble()) * 100
        return percentage >= MIN_ALLOCATED_HEAP_LEFT_PERCENTAGE
    }
}
