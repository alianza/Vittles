package com.example.vittles.scanning

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.vittles.services.scanner.ScanningService
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.util.concurrent.TimeUnit

/**
 * Analyzer used by CameraX to analyze frames from the camera preview.
 *
 * @author Jeroen Flietstra
 *
 * @property onBarcodeSuccess Callback function for successful scan.
 * @property onBarcodeFailure Callback function for unsuccessful scan.
 */
class PreviewAnalyzer(
    private val onBarcodeFailure: (exception: Exception) -> Unit,
    private val onBarcodeSuccess: (barcodes: List<FirebaseVisionBarcode>) -> Unit,
    private val onOcrFailure: (exception: Exception) -> Unit,
    private val onOcrSuccess: (text: String) -> Unit
) : ImageAnalysis.Analyzer {
    // Value used for the scanning delay
    private var lastAnalyzedTimestamp = 0L

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
        // Scan only every 500 ms instead of every frame.
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >=
            TimeUnit.MILLISECONDS.toMillis(500)
        ) {
            val mediaImage = imageProxy?.image
            val imageRotation = degreesToFirebaseRotation(degrees)
            if (mediaImage != null) {
                val image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
                ScanningService.scanForBarcode(image, onBarcodeSuccess, onBarcodeFailure)
                ScanningService.scanForExpirationDate(image, onOcrSuccess, onOcrFailure)
            }
            lastAnalyzedTimestamp = currentTimestamp
        }
    }
}
