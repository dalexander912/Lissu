package com.lissu.data

data class ShoppingList(
    val id: String,
    val name: String,
    val items: List<Item> = emptyList()
)

data class Item(
    val id: String,
    val name: String,
    val isChecked: Boolean = false
)
