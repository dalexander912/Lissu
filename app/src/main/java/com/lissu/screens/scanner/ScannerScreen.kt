package com.lissu.screens.scanner

import android.Manifest
import android.R.color.white
import android.content.pm.PackageManager
import android.graphics.Color.WHITE
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.lissu.AppScaffold
import com.lissu.Routes

@Composable
fun ScannerScreen(onBack: () -> Unit) {
    val viewModel: ScannerScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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

    AppScaffold(
        title = "Scanner",
        currentScreen = Routes.Scanner,
        navigationIcon = {
            IconButton(onClick = {
                viewModel.resetScan()
                onBack()

            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
        }

    ){ innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Escaner", style = MaterialTheme.typography.headlineSmall)

            // Instanciar la vista que procesa  el hardware y la IA
            if (hasCameraPermission && !uiState.scanned) {
                CameraBarcodeScanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    onBarcodeDetected = { barcode ->
                        viewModel.onBarcodeScanned(barcode)
                    }
                )
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            AnimatedVisibility(visible = uiState.scanned && !uiState.isLoading) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Código de barras: ${uiState.barcode}", style = MaterialTheme.typography.bodyMedium)

                    uiState.scannedItem?.let { item ->
                        if (!item.imageUrl.isNullOrEmpty()) {
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
                            value = item.category ?: "",
                            onValueChange = viewModel::onCategoryChange,
                            label = { Text("Categoría") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    uiState.error?.let { msg ->
                        Text(msg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
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
}