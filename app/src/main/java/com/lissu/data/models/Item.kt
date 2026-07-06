package com.lissu.data.models

data class Item(
    val id: String,
    val name: String,
    val isChecked: Boolean = false,
    val category: String? = null,
    val imageUrl: String? = null,
    val brand: String? = null
)