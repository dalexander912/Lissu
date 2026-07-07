package com.lissu.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScannerScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAddList: () -> Unit,
    onNavigateToMaps: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToReminders: () -> Unit
) {
    val viewModel: ScannerScreenViewModel = viewModel(factory = ScannerScreenViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

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

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ScannerScreenViewModel.UiEvent.SaveSuccess -> {
                    snackbarHostState.showSnackbar("añadido con éxito")
                }
                is ScannerScreenViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    AppScaffold(
        title = "Scanner",
        currentScreen = Routes.Scanner,
        snackbarHostState = snackbarHostState,
        onNavigateToHome = onNavigateToHome,
        onNavigateToAddList = onNavigateToAddList,
        onNavigateToMaps = onNavigateToMaps,
        onNavigateToAccount = onNavigateToAccount,
        onNavigateToReminders = onNavigateToReminders,
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Escaner", style = MaterialTheme.typography.headlineSmall)

            // Instanciar la vista que procesa  el hardware y la IA
            //ocultar camara si popup esta abierto
            if (hasCameraPermission && !uiState.scanned && !uiState.showListSelector) {
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
                        Button(
                            onClick = viewModel::onShowListSelector,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Añadir a lista")
                        }
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

    if (uiState.showListSelector) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissListSelector,
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = {
                Text(
                    text = "Selecciona listas",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.shoppingLists.forEach { list ->
                        val isSelected = uiState.selectedListIds.contains(list.id)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.toggleListSelection(list.id) },
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { viewModel.toggleListSelection(list.id) }
                                )
                                Text(
                                    text = list.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = viewModel::addItemsToSelectedLists,
                    enabled = uiState.selectedListIds.isNotEmpty()
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissListSelector) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}
