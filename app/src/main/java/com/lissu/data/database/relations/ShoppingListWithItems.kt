package com.lissu.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.lissu.data.database.entities.ItemEntity
import com.lissu.data.database.entities.ShoppingListEntity
import com.lissu.data.database.entities.toModel
import com.lissu.data.models.ShoppingList

data class ShoppingListWithItems(
    @Embedded val shoppingList: ShoppingListEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ItemEntity>
)

fun ShoppingListWithItems.toModel(): ShoppingList {
    return shoppingList.toModel().copy(
        items = items.map { it.toModel() }
    )
}
