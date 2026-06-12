package com.lissu.data

import android.content.Context
import com.lissu.data.database.AppDatabase
import com.lissu.data.repositories.ReminderRepository
import com.lissu.data.repositories.ReminderRepositoryImpl

class AppProvider(context: Context) {

  private val appDatabase = AppDatabase.getDatabase(context)
  private val reminderDao = appDatabase.reminderDao()

  private val reminderRepository: ReminderRepository =
    ReminderRepositoryImpl(reminderDao)

  fun provideReminderRepository(): ReminderRepository {
    return reminderRepository
  }
}