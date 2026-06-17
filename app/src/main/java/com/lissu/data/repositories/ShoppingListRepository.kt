package com.lissu.data.repositories

import com.lissu.data.models.Item
import com.lissu.data.models.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object ShoppingListRepository {
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(
        listOf(
            ShoppingList(
                "1", "Lista 1", listOf(
                    Item("1", "Lechuga"),
                    Item("2", "Tomate"),
                    Item("3", "Huevos"),
                    Item("4", "Pan"),
                    Item("5", "Leche"),
                    Item("6", "Queso")
                )
            ),
            ShoppingList(
                "2", "Lista 2", listOf(
                    Item("7", "Arroz"),
                    Item("8", "Frijoles"),
                    Item("9", "Café"),
                    Item("10", "Leche")
                )
            )
        )
    )
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists.asStateFlow()

    fun addItemToList(listId: String, itemName: String) {
        _shoppingLists.update { lists ->
            lists.map { list ->
                if (list.id == listId) {
                    val newItem = Item(id = UUID.randomUUID().toString(), name = itemName)
                    list.copy(items = list.items + newItem)
                } else {
                    list
                }
            }
        }
    }

    fun deleteList(listId: String) {
        _shoppingLists.update { currentLists ->
            currentLists.filter { it.id != listId }
        }
    }
}