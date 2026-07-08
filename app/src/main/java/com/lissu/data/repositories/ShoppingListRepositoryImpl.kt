package com.lissu.data.repositories

import com.lissu.data.database.dao.ItemDao
import com.lissu.data.database.dao.ShoppingListDao
import com.lissu.data.database.entities.toEntity
import com.lissu.data.database.relations.toModel
import com.lissu.data.models.Item
import com.lissu.data.models.ShoppingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao,
    private val itemDao: ItemDao
) : ShoppingListRepository {

    override fun getShoppingListsWithItems(): Flow<List<ShoppingList>> {
        return shoppingListDao.getShoppingListsWithItems().map { relations ->
            relations.map { it.toModel() }
        }
    }

    override fun getShoppingListById(id: String): Flow<ShoppingList?> {
        return shoppingListDao.getShoppingListWithItemsById(id).map { it?.toModel() }
    }

    override suspend fun insertShoppingList(shoppingList: ShoppingList) {
        shoppingListDao.upsertShoppingList(shoppingList.toEntity())
    }

    override suspend fun deleteShoppingList(shoppingList: ShoppingList) {
        shoppingListDao.deleteShoppingList(shoppingList.toEntity())
    }

    override suspend fun addItemToList(listId: String, item: Item) {
        itemDao.upsertItem(item.toEntity(listId))
    }

    override suspend fun updateItem(item: Item, listId: String) {
        itemDao.upsertItem(item.toEntity(listId))
    }

    override suspend fun deleteItem(item: Item, listId: String) {
        itemDao.deleteItem(item.toEntity(listId))
    }
}
