package com.lissu.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.lissu.data.database.entities.ShoppingListEntity
import com.lissu.data.database.relations.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists")
    fun getAllShoppingLists(): Flow<List<ShoppingListEntity>>

    @Transaction
    @Query("SELECT * FROM shopping_lists")
    fun getShoppingListsWithItems(): Flow<List<ShoppingListWithItems>>

    @Transaction
    @Query("SELECT * FROM shopping_lists WHERE id = :listId")
    fun getShoppingListWithItemsById(listId: String): Flow<ShoppingListWithItems?>

    @Upsert
    suspend fun upsertShoppingList(shoppingList: ShoppingListEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShoppingList(shoppingList: ShoppingListEntity): Long

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingListEntity)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingListEntity)
}
