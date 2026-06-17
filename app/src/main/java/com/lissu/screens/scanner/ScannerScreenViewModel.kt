package com.lissu.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lissu.LissuApplication
import com.lissu.data.api.KtorClient
import com.lissu.data.repositories.ApiRepository
import com.lissu.data.repositories.ItemInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.lissu.data.models.Item
import com.lissu.data.repositories.ShoppingListRepository
import com.lissu.data.models.ShoppingList
import java.util.UUID
data class ScannerUiState(
    val barcode: String = "",
    val scannedItem: Item? = null,
    val isLoading: Boolean = false,
    val scanned: Boolean = false,
    val error: String? = null,
    val showListSelector: Boolean = false,
    val shoppingLists: List<ShoppingList> = emptyList()
)

class ScannerScreenViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

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


    fun onShowListSelector() {
        viewModelScope.launch {
            shoppingListRepository.getShoppingListsWithItems().collect { lists ->
                _uiState.update { it.copy(showListSelector = true, shoppingLists = lists) }
            }
        }
    }

    fun onDismissListSelector() {
        _uiState.update { it.copy(showListSelector = false) }
    }


    fun addItemToList(listId: String) {
        val scannedItem = _uiState.value.scannedItem ?: return

        viewModelScope.launch {

            val itemToSave = scannedItem.copy(id = UUID.randomUUID().toString())
            shoppingListRepository.addItemToList(listId, itemToSave)

            _uiState.update { it.copy(showListSelector = false, scanned = false, scannedItem = null) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as LissuApplication
                ScannerScreenViewModel(app.appProvider.provideShoppingListRepository())
            }
        }
    }
}