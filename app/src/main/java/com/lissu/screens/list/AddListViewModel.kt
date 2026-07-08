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
        private set
    var listName by mutableStateOf("Nueva Lista")
        private set
    var assignedDay by mutableStateOf<String?>(null)
        private set
    val items = mutableStateListOf<Item>()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        object SaveSuccess : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }

    fun loadList(id: String?) {
        viewModelScope.launch {
            if (id == null) {
                val newId = UUID.randomUUID().toString()
                listId = newId
                listName = "Nueva Lista"
                assignedDay = null
                items.clear()
                // Crear la lista inicial en la DB
                autoSave()
                return@launch
            }

            listId = id
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

    fun updateListName(newName: String) {
        listName = newName
        autoSave()
    }

    fun updateAssignedDay(day: String?) {
        assignedDay = day
        autoSave()
    }

    private fun autoSave() {
        val currentId = listId ?: return
        viewModelScope.launch {
            val shoppingList = ShoppingList(
                id = currentId,
                name = listName,
                assignedDay = assignedDay
            )
            shoppingListRepository.insertShoppingList(shoppingList)
        }
    }

    fun addItem(name: String) {
        if (name.isNotBlank()) {
            val lid = listId ?: return
            val newItem = Item(id = UUID.randomUUID().toString(), name = name)
            items.add(newItem)

            viewModelScope.launch {
                shoppingListRepository.addItemToList(lid, newItem)
            }
        }
    }

    fun toggleItem(id: String) {
        val lid = listId ?: return
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = items[index]
            val updatedItem = item.copy(isChecked = !item.isChecked)
            items[index] = updatedItem

            viewModelScope.launch {
                shoppingListRepository.updateItem(updatedItem, lid)
            }
        }
    }

    fun removeItem(id: String) {
        val lid = listId ?: return
        val itemToRemove = items.find { it.id == id }
        items.removeIf { it.id == id }

        itemToRemove?.let { item ->
            viewModelScope.launch {
                shoppingListRepository.deleteItem(item, lid)
            }
        }
    }

    fun saveList() {
        autoSave()
        viewModelScope.launch {
            _uiEvent.send(UiEvent.SaveSuccess)
        }
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
