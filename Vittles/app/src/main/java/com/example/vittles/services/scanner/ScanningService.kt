package com.example.vittles.services.scanner

import android.annotation.SuppressLint
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

/**
 * Service for scanning barcodes and recognizing text.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Marc van Spronsen
 */
object ScanningService {

    /** Regex to match against dates (12/12/19, 12-12-2019, 12.dec.19, 12:12:2019, 12-nov, 12-11). */
    private val regex = Regex("(?<![a-zA-Z0-9]|[\\/\\-:])((?:(?:31([\\/\\-.: ])(?:0[13578]|1[02]|(?:jan|mar|may|jul|aug|oct|dec|okt|mei|mrt)))[\\/\\-.: ]|(?:(?:29|30)([\\/\\-.: ])(?:0[1,3-9]|1[0-2]|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|okt|mei|mrt))[\\/\\-.: ]))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})|(?:29([\\/\\-.: ])(?:02|(?:feb))[\\/\\-.: ](?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:0[1-9]|1\\d|2[0-8])([\\/\\-.: ])(?:(?:0[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep|mei|mrt))|(?:1[0-2]|(?:oct|nov|dec|okt)))[\\/\\-.: ](?:(?:1[6-9]|[2-9]\\d)?\\d{2}))(?![a-zA-Z0-9]|[\\/\\-:])|" +
            "(?<![a-zA-Z0-9]|[\\/\\-:])((?:(?:31([\\/\\-.: ])(?:0[13578]|1[02]|(?:jan|mar|may|jul|aug|oct|dec|okt|mei|mrt)))|(?:(?:29|30)([\\/\\-.: ])(?:0[1,3-9]|1[0-2]|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|okt|mei|mrt))[\\/\\-.: ]))|(?:29([\\/\\-.: ])(?:02|(?:feb)))((?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:0[1-9]|1\\d|2[0-8])([\\/\\-.: ])(?:(?:0[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep|mei|mrt))|(?:1[0-2]|(?:oct|nov|dec|okt))))(?![a-zA-Z0-9]|[\\/\\-:])")
    /** Regex to match against dates (12-2019, dec.2019, 12:2019, okt-2021)/ */
    private val shortRegex = Regex("(?<![A-Za-z0-9]|[\\/\\-: ])(?:(0[1-9]{1}|1[0-2]{1})([\\/\\-.: ]\\d{4}))(?![A-Za-z0-9]|[\\/\\-: ])|" +
            "(?<![A-Za-z0-9]|[\\/\\-: ])(?:jan|feb|mar|apr|may|jun|jul|aug|oct|nov|dec|okt|mei|mrt{3})([\\/\\-.: ]\\d{4})(?![A-Za-z0-9]|[\\/\\-: ])")

    /** The firebase detector for optical text recognition. **/
    private val ocrDetector = FirebaseVision.getInstance() .onDeviceTextRecognizer

    /** @suppress **/
    // Only scan EAN codes, add more if needed.
    private val options = FirebaseVisionBarcodeDetectorOptions.Builder()
        .setBarcodeFormats(
            FirebaseVisionBarcode.FORMAT_EAN_13,
            FirebaseVisionBarcode.FORMAT_EAN_8,
            FirebaseVisionBarcode.FORMAT_UPC_A,
            FirebaseVisionBarcode.FORMAT_UPC_E,
            FirebaseVisionBarcode.FORMAT_ITF,
            FirebaseVisionBarcode.TYPE_ISBN
        )
        .build()
    /** The firebase detector for barcodes. **/
    private val barcodeDetector = FirebaseVision.getInstance()
        .getVisionBarcodeDetector(options)

    /**
     * Scans the image for barcodes and retrieves the value from the barcodes to return it to
     * the callback function.
     *
     * @param image The frame that will be scanned.
     * @param onBarcodesSuccess Callback function for successful scan.
     * @param onBarcodesFailure Callback function for unsuccessful scan.
     *
     * @return Task of the detection.
     */
    fun scanForBarcode(
        image: FirebaseVisionImage,
        onBarcodesSuccess: (barcodes: List<FirebaseVisionBarcode>) -> Unit,
        onBarcodesFailure: (exception: Exception) -> Unit
    ): Task<MutableList<FirebaseVisionBarcode>> {
        return barcodeDetector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                onBarcodesSuccess(barcodes)
            }
            .addOnFailureListener {
                onBarcodesFailure(it)
            }
    }

    /**
     * Scans the image for expiration dates to return it to
     * the callback function.
     *
     * @param image The frame that will be scanned.
     * @param onOcrSuccess Callback function for successful scan.
     * @param onOcrFailure Callback function for unsuccessful scan.
     *
     * @return Task of the detection.
     */
    @SuppressLint("DefaultLocale")
    fun scanForExpirationDate(
        image: FirebaseVisionImage,
        onOcrSuccess: (text: String) -> Unit,
        onOcrFailure: (exception: Exception) -> Unit
    ): Task<FirebaseVisionText> {
                return ocrDetector.processImage(image)
                    .addOnSuccessListener { firebaseVisionText ->
                        var matchedText = regex.find(firebaseVisionText.text.toLowerCase(), 0)

                        if (matchedText == null) {
                            matchedText = shortRegex.find(firebaseVisionText.text.toLowerCase(),0)
                        }

                        if (matchedText !== null) {
                            println("MatchedText " + matchedText.value)
                            onOcrSuccess(matchedText.value)
                        }

                    }
                    .addOnFailureListener {
                        onOcrFailure(it)
                    }
        }
}