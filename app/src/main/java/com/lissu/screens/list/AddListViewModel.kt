package com.lissu.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lissu.LissuApplication
import com.lissu.data.models.Item
import com.lissu.data.models.ShoppingList
import com.lissu.data.repositories.ShoppingListRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddListViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    var listId by mutableStateOf<String?>(null)
    var listName by mutableStateOf("Nueva Lista")
    var assignedDay by mutableStateOf<String?>(null)
    val items = mutableStateListOf<Item>()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        object SaveSuccess : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }

    fun loadList(id: String?) {
        if (id == null) {
            listId = null
            listName = "Nueva Lista"
            assignedDay = null
            items.clear()
            return
        }

        // Si ya está cargada
        if (listId == id) return

        listId = id
        viewModelScope.launch {
            // Obtener la lista con sus items en la DB
            val list = shoppingListRepository.getShoppingListById(id).first()
            list?.let {
                listName = it.name
                assignedDay = it.assignedDay
                items.clear()
                items.addAll(it.items)
            }
        }
    }

    fun addItem(name: String) {
        if (name.isNotBlank()) {
            val newItem = Item(id = UUID.randomUUID().toString(), name = name)
            items.add(newItem)

            // Si la lista ya existe en la DB, guardamos el item
            listId?.let { lid ->
                viewModelScope.launch {
                    shoppingListRepository.addItemToList(lid, newItem)
                }
            }
        }
    }

    fun toggleItem(id: String) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = items[index]
            val updatedItem = item.copy(isChecked = !item.isChecked)
            items[index] = updatedItem

            // Actualizar en DB si la lista existe
            listId?.let { lid ->
                viewModelScope.launch {
                    shoppingListRepository.updateItem(updatedItem, lid)
                }
            }
        }
    }

    fun removeItem(id: String) {
        val itemToRemove = items.find { it.id == id }
        items.removeIf { it.id == id }

        listId?.let { lid ->
            itemToRemove?.let { item ->
                viewModelScope.launch {
                    shoppingListRepository.deleteItem(item, lid)
                }
            }
        }
    }

    fun saveList() {
        val currentId = listId ?: UUID.randomUUID().toString()
        listId = currentId

        val shoppingList = ShoppingList(
            id = currentId,
            name = listName,
            assignedDay = assignedDay
        )

        viewModelScope.launch {
            shoppingListRepository.insertShoppingList(shoppingList)
            // Guarda todos los items actuales
            items.forEach { item ->
                shoppingListRepository.addItemToList(currentId, item)
            }
            _uiEvent.send(UiEvent.SaveSuccess)
        }
    }

    fun updateAssignedDay(day: String?) {
        assignedDay = day
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as LissuApplication
                AddListViewModel(app.appProvider.provideShoppingListRepository())
            }
        }
    }
}
