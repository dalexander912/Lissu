package com.lissu.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lissu.data.models.ShoppingList

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val assignedDay: String? = null
)

fun ShoppingListEntity.toModel(): ShoppingList {
    return ShoppingList(
        id = id,
        name = name,
        assignedDay = assignedDay,
        items = emptyList()
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        id = id,
        name = name,
        assignedDay = assignedDay
    )
}
