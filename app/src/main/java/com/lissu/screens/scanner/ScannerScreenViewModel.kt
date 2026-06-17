package com.lissu.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lissu.data.api.KtorClient
import com.lissu.data.repository.ApiRepository
import com.lissu.data.repository.ItemInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.lissu.data.models.Item

data class ScannerUiState(
    val barcode: String = "",
    val scannedItem: Item? = null,
    val isLoading: Boolean = false,
    val scanned: Boolean = false,
    val error: String? = null
)

class ScannerScreenViewModel() : ViewModel() {

    private val repository: ItemInterface = ApiRepository(KtorClient.client)

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    fun onBarcodeScanned(barcode: String) {

        if (_uiState.value.isLoading || _uiState.value.scanned) return

        _uiState.update { it.copy(isLoading = true, barcode = barcode, error = null) }

        viewModelScope.launch {
            repository.fetchItemByBarcode(barcode)
                .onSuccess { item ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            scanned = true,
                            scannedItem = item
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            scanned = true,
                            error = exception.message ?: "Error desconocido al buscar el producto"
                        )
                    }
                }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { state ->
            state.copy(scannedItem = state.scannedItem?.copy(name = newName))
        }
    }

    fun onCategoryChange(newCategory: String) {
        _uiState.update { state ->
            state.copy(scannedItem = state.scannedItem?.copy(category = newCategory))
        }
    }

    fun resetScan() {
        _uiState.value = ScannerUiState()
    }
}