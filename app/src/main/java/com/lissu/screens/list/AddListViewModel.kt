package com.lissu.screens.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lissu.data.Item
import com.lissu.data.ShoppingList

class AddListViewModel : ViewModel() {
    var listName by mutableStateOf("Nueva Lista")
    val items = mutableStateListOf<Item>()

    private val mockLists = listOf(
        ShoppingList(
            id = "1",
            name = "Lista 1",
            items = listOf(
                Item("1", "Lechuga"),
                Item("2", "Tomate"),
                Item("3", "Huevos"),
                Item("4", "Pan"),
                Item("5", "Leche"),
                Item("6", "Queso")
            )
        ),
        ShoppingList(
            id = "2",
            name = "Lista 2",
            items = listOf(
                Item("7", "Arroz"),
                Item("8", "Frijoles"),
                Item("9", "Café"),
                Item("10", "Leche")
            )
        )
    )

    fun loadList(id: String?) {
        if (id == null) {
            listName = "Nueva Lista"
            items.clear()
        } else {
            val list = mockLists.find { it.id == id }
            if (list != null) {
                listName = list.name
                items.clear()
                items.addAll(list.items)
            }
        }
    }

    fun addItem(name: String) {
        if (name.isNotBlank()) {
            val nextId = (items.size + 1).toString()
            items.add(Item(nextId, name))
        }
    }

    fun toggleItem(id: String) {
        val index = items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = items[index]
            items[index] = item.copy(isChecked = !item.isChecked)
        }
    }

    fun removeItem(id: String) {
        items.removeIf { it.id == id }
    }
}
