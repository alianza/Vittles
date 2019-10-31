package com.example.vittles.services.scanner

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage

object ScanningService {

    private var barcodeValue: String? = null

    fun scanForBarcode(image: FirebaseVisionImage) : String? {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(
                FirebaseVisionBarcode.FORMAT_EAN_13,
                FirebaseVisionBarcode.FORMAT_EAN_8)
            .build()
        val detector = FirebaseVision.getInstance()
            .getVisionBarcodeDetector(options)
        val result = detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    if(barcode.rawValue != barcodeValue) {
                        barcodeValue = barcode.rawValue
                        println(barcodeValue)
                    }
                }
            }
            .addOnFailureListener {

            }
        return barcodeValue
    }
}