package com.lissu.screens.scanner

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executors

@Composable
fun CameraBarcodeScanner(
    modifier: Modifier = Modifier,
    onBarcodeDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Executor  ML Kit procese frames sin congelar la interfaz
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var lastScanned by remember { mutableStateOf("") }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)

            // Iniciar cliente de Inteligencia Artificial (ML Kit)
            val scanner = BarcodeScanning.getClient(
                com.google.mlkit.vision.barcode.BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .build()
            )

            // Llamamos funcion de ciclo de vida de la camara
            CameraLifecycle(
                context = ctx,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                cameraExecutor = cameraExecutor,
                analyzer = { imageProxy ->
                    processImageProxy(
                        scanner = scanner,
                        imageProxy = imageProxy,
                        onBarcodeDetected = { barcode ->


                            if (barcode != lastScanned) {
                                lastScanned = barcode
                                onBarcodeDetected(barcode)
                            }
                        }
                    )
                }
            )

            previewView
        }
    )

    // Liberación del hilo de la cámara al destruir el Composable
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}

//recibe un paquete de píxeles (ImageProxy), lo convierte al
//formato de Google (InputImage), corre los modelos matemáticos para buscar barras negras y blancas, y devuelve String
@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    scanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onBarcodeDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = com.google.mlkit.vision.common.InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { value ->
                        onBarcodeDetected(value)
                    }
                }
            }
            .addOnFailureListener { it.printStackTrace() }
            .addOnCompleteListener { imageProxy.close() }
    } else {
        imageProxy.close()
    }
}