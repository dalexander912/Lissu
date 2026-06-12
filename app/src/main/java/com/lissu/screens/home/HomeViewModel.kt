package com.lissu.screens.home

import androidx.lifecycle.ViewModel
import com.lissu.data.Item
import com.lissu.data.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(
        listOf(
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
    )
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists.asStateFlow()

    fun deleteList(list: ShoppingList) {
        _shoppingLists.update { currentLists ->
            currentLists.filter { it.id != list.id }
        }
    }
}
