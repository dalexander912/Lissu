package com.lissu.screens.list

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.lissu.data.Item

class AddListViewModel : ViewModel() {
    var listName = "Lista 3"
    val items = mutableStateListOf<Item>(
        Item("1", "item n", true),
        Item("2", "item n2", true),
        Item("3", "item n3", false),
        Item("4", "item n4", false)
    )

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
