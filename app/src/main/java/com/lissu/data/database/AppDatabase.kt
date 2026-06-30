package com.lissu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lissu.data.database.dao.ItemDao
import com.lissu.data.database.dao.ReminderDao
import com.lissu.data.database.dao.ShoppingListDao
import com.lissu.data.database.entities.ItemEntity
import com.lissu.data.database.entities.ReminderEntity
import com.lissu.data.database.entities.ShoppingListEntity

@Database(
  entities = [
    ReminderEntity::class,
    ShoppingListEntity::class,
    ItemEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun reminderDao(): ReminderDao
  abstract fun shoppingListDao(): ShoppingListDao
  abstract fun itemDao(): ItemDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        Room.databaseBuilder(
          context = context.applicationContext,
          klass = AppDatabase::class.java,
          name = "lissu_database"
        )
          .fallbackToDestructiveMigration(true)
          .build()
          .also { INSTANCE = it }
      }
    }
  }
}
