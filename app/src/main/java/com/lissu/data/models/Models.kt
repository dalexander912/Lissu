package com.lissu.data.models

data class ShoppingList(
    val id: String,
    val name: String,
    val items: List<Item> = emptyList()
)

data class Item(
    val id: String,
    val name: String,
    val isChecked: Boolean = false,
    val category: String,
    val imageUrl: String,
    val brand: String
)
