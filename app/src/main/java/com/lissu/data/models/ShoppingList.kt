package com.lissu.data.models

data class ShoppingList(
    val id: String,
    val name: String,
    val items: List<Item> = emptyList()
)
