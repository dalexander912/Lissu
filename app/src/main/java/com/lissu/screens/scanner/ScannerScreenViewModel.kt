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
import kotlinx.coroutines.channels.Channel
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow

data class ScannerUiState(
    val barcode: String = "",
    val scannedItem: Item? = null,
    val isLoading: Boolean = false,
    val scanned: Boolean = false,
    val error: String? = null,
    val showListSelector: Boolean = false,
    val shoppingLists: List<ShoppingList> = emptyList(),
    val selectedListIds: Set<String> = emptySet(),
)

class ScannerScreenViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val repository: ItemInterface = ApiRepository(KtorClient.client)

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveSuccess : UiEvent()
    }

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

            //usar first() para obtener el primer valor y no seguir escuchando cambios
            val lists = shoppingListRepository.getShoppingListsWithItems().first()

            _uiState.update { it.copy(showListSelector = true, shoppingLists = lists, selectedListIds = emptySet()) }

        }
    }

    fun onDismissListSelector() {
        _uiState.update { it.copy(showListSelector = false, selectedListIds = emptySet()) }
    }

    fun toggleListSelection(listId: String) {
        _uiState.update { state ->
            val newSelected = if (state.selectedListIds.contains(listId)) {
                state.selectedListIds - listId
            } else {
                state.selectedListIds + listId
            }
            state.copy(selectedListIds = newSelected)
        }
    }

    fun addItemsToSelectedLists() {
        val scannedItem = _uiState.value.scannedItem ?: return
        val selectedIds = _uiState.value.selectedListIds
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            selectedIds.forEach { listId ->
                val itemToSave = scannedItem.copy(id = UUID.randomUUID().toString())
                shoppingListRepository.addItemToList(listId, itemToSave)
            }

            //cerrar el selector y resetear el estado del escaner
            _uiState.update { it.copy(
                showListSelector = false,
                scanned = false,
                scannedItem = null,
                selectedListIds = emptySet())
            }
            _uiEvent.send(UiEvent.SaveSuccess)
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
