package com.lissu.screens.scanner

import androidx.compose.runtime.Composable

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(onBack: () -> Unit) {
    val viewModel: ScannerScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Permisos de la cámara obligatorios
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Barra superior básica
        IconButton(onClick = {
            viewModel.resetScan()
            onBack()
        }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }

        Text("Escáner de Ítems", style = MaterialTheme.typography.headlineSmall)

        // 1. Mostrar la cámara únicamente si hay permisos y no se ha completado un escaneo masivo
        if (hasCameraPermission && !uiState.scanned) {
            CameraBarcodeScannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp)),
                onBarcodeDetected = { barcode ->
                    viewModel.onBarcodeScanned(barcode)
                }
            )
        }

        // 2. Loader si está pegándole a Ktor
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        // 3. Formulario dinámico cuando ya tenemos respuesta
        AnimatedVisibility(visible = uiState.scanned && !uiState.isLoading) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Código de barras: ${uiState.barcode}", style = MaterialTheme.typography.bodyMedium)

                uiState.scannedItem?.let { item ->
                    if (item.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = "Imagen del item",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }

                    OutlinedTextField(
                        value = item.name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text("Nombre del Ítem") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = item.category,
                        onValueChange = viewModel::onCategoryChange,
                        label = { Text("Categoría") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Mostrar mensaje en caso de que Ktor falle o no encuentre el producto
                uiState.error?.let { msg ->
                    Text(msg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    Text("Puedes llenar los datos manualmente.", style = MaterialTheme.typography.bodySmall)

                    // Si no trajo ítem de la API por error, podrías inicializar campos vacíos aquí si gustas
                }

                Button(
                    onClick = viewModel::resetScan,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Escanear otro ítem")
                }
            }
        }
    }
}

@Composable
fun CameraBarcodeScannerView(
    modifier: Modifier = Modifier,
    onBarcodeDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var lastScanned by remember { mutableStateOf("") }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Configuración del flujo visual (Preview)
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Configuración del procesador de ML Kit
                val scanner = BarcodeScanning.getClient(
                    com.google.mlkit.vision.barcode.BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build()
                )

                // Configuración del análisis de imágenes por detrás
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
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

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
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