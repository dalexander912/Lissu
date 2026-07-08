package com.lissu.data.models

data class ShoppingList(
    val id: String,
    val name: String,
    val assignedDay: String? = null,
    val items: List<Item> = emptyList()
)
