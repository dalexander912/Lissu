package com.lissu.data.repositories

import com.lissu.data.models.Item
import com.lissu.data.models.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getShoppingListsWithItems(): Flow<List<ShoppingList>>
    suspend fun insertShoppingList(shoppingList: ShoppingList)
    suspend fun deleteShoppingList(shoppingList: ShoppingList)
    suspend fun addItemToList(listId: String, item: Item)
    suspend fun updateItem(item: Item, listId: String)
    suspend fun deleteItem(item: Item, listId: String)
}
