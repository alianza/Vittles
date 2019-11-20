package com.example.vittles.services.scanner

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.lang.Exception
import java.util.*

/**
 * Service for scanning barcodes and recognizing text.
 *
 * @author Jeroen Flietstra
 * @author Marc van Spronsen
 */
object ScanningService {

    //    Regex to match against dates (12/12/19, 12-12-2019, 12.dec.19, 12:12:2019, 12-nov, 12-11)
    private val regex = Regex("(?<![a-zA-Z0-9]|[\\/\\-.:])((?:(?:31([\\/\\-.: ])(?:0[13578]|1[02]|(?:jan|mar|may|jul|aug|oct|dec|okt|mei|mrt)))[\\/\\-.: ]|(?:(?:29|30)([\\/\\-.: ])(?:0[1,3-9]|1[0-2]|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|okt|mei|mrt))[\\/\\-.: ]))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})|(?:29([\\/\\-.: ])(?:02|(?:feb))[\\/\\-.: ](?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:0[1-9]|1\\d|2[0-8])([\\/\\-.: ])(?:(?:0[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep|mei|mrt))|(?:1[0-2]|(?:oct|nov|dec|okt)))[\\/\\-.: ](?:(?:1[6-9]|[2-9]\\d)?\\d{2}))(?![a-zA-Z0-9]|[\\/\\-.:])|" +
            "(?<![a-zA-Z0-9]|[\\/\\-.:])((?:(?:31([\\/\\-.: ])(?:0[13578]|1[02]|(?:jan|mar|may|jul|aug|oct|dec|okt|mei|mrt)))|(?:(?:29|30)([\\/\\-.: ])(?:0[1,3-9]|1[0-2]|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec|okt|mei|mrt))[\\/\\-.: ]))|(?:29([\\/\\-.: ])(?:02|(?:feb)))((?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))|(?:0[1-9]|1\\d|2[0-8])([\\/\\-.: ])(?:(?:0[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep|mei|mrt))|(?:1[0-2]|(?:oct|nov|dec|okt))))(?![a-zA-Z0-9]|[\\/\\-.:])")
    //    Regex to match against dates (12-2019, dec.2019, 12:2019, okt-2021)
    private val shortRegex = Regex("(?<![A-Za-z0-9]|[\\/\\-.: ])(?:(0[1-9]{1}|1[0-2]{1})([\\/\\-.: ]\\d{4}))(?![A-Za-z0-9]|[\\/\\-.: ])|" +
            "(?<![A-Za-z0-9]|[\\/\\-.: ])(?:jan|feb|mar|apr|may|jun|jul|aug|oct|nov|dec|okt|mei|mrt{3})([\\/\\-.: ]\\d{4})(?![A-Za-z0-9]|[\\/\\-.: ])")

    /**
     * Scans the image for barcodes and retrieves the value from the barcodes to return it to
     * the callback function.
     *
     * @param image The frame that will be scanned.
     * @param onBarcodesSuccess Callback function for successful scan.
     * @param onBarcodesFailure Callback function for unsuccessful scan.
     */
    fun scanForBarcode(
        image: FirebaseVisionImage,
        onBarcodesSuccess: (barcodes: List<FirebaseVisionBarcode>) -> Unit,
        onBarcodesFailure: (exception: Exception) -> Unit
    ) {
        // Only scan EAN codes, add more if needed.
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_EAN_13,
                FirebaseVisionBarcode.FORMAT_EAN_8,
                FirebaseVisionBarcode.FORMAT_UPC_A,
                FirebaseVisionBarcode.FORMAT_UPC_E,
                FirebaseVisionBarcode.FORMAT_ITF,
                FirebaseVisionBarcode.TYPE_ISBN
            )
            .build()
        val detector = FirebaseVision.getInstance()
            .getVisionBarcodeDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                onBarcodesSuccess(barcodes)
            }
            .addOnFailureListener {
                onBarcodesFailure(it)
            }
    }

    fun scanForExpirationDate(
        image: FirebaseVisionImage,
        onOcrSuccess: (text: String) -> Unit,
        onOcrFailure: (exception: Exception) -> Unit
    ) {
        val detector = FirebaseVision.getInstance() .onDeviceTextRecognizer

                detector.processImage(image)
                    .addOnSuccessListener { firebaseVisionText ->
                        var matchedText = regex.find(firebaseVisionText.text.toLowerCase(), 0)

                        println("ScannedText " + firebaseVisionText.text)

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

//        val scannedText = "ten minste houdbaar tot28.08.15niet ongekoeld bewaren"
//
//        val matchedText = regex.find(scannedText.toLowerCase(), 0)
//
//        if (matchedText !== null) {
//            println("MatchedText ${matchedText.value}")
//            onOcrSuccess(matchedText.value)
//        }
        }
}