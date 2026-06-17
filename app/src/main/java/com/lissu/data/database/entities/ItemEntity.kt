package com.lissu.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lissu.data.models.Item

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listId"])]
)
data class ItemEntity(
    @PrimaryKey
    val id: String,
    val listId: String,
    val name: String,
    val isChecked: Boolean = false,
    val category: String? = null,
    val imageUrl: String? = null,
    val brand: String? = null
)

fun ItemEntity.toModel(): Item {
    return Item(
        id = id,
        name = name,
        isChecked = isChecked,
        category = category,
        imageUrl = imageUrl,
        brand = brand
    )
}

fun Item.toEntity(listId: String): ItemEntity {
    return ItemEntity(
        id = id,
        listId = listId,
        name = name,
        isChecked = isChecked,
        category = category,
        imageUrl = imageUrl,
        brand = brand
    )
}
