package com.lissu.data

import android.content.Context
import com.lissu.data.database.AppDatabase
import com.lissu.data.repositories.AuthRepository
import com.lissu.data.repositories.AuthRepositoryImpl
import com.lissu.data.repositories.ReminderRepository
import com.lissu.data.repositories.ReminderRepositoryImpl
import com.lissu.data.repositories.ShoppingListRepository
import com.lissu.data.repositories.ShoppingListRepositoryImpl
import com.lissu.data.session.SessionManager

class AppProvider(context: Context) {

  private val appDatabase = AppDatabase.getDatabase(context)
  private val reminderDao = appDatabase.reminderDao()
  private val shoppingListDao = appDatabase.shoppingListDao()
  private val itemDao = appDatabase.itemDao()
  private val sessionManager = SessionManager(context)

  private val reminderRepository: ReminderRepository =
    ReminderRepositoryImpl(reminderDao)

  private val shoppingListRepository: ShoppingListRepository =
    ShoppingListRepositoryImpl(shoppingListDao, itemDao)

  private val authRepository: AuthRepository =
    AuthRepositoryImpl(sessionManager)

  fun provideReminderRepository(): ReminderRepository {
    return reminderRepository
  }

  fun provideShoppingListRepository(): ShoppingListRepository {
    return shoppingListRepository
  }

  fun provideAuthRepository(): AuthRepository {
    return authRepository
  }
}
