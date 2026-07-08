package com.lissu.data.repositories

import com.lissu.data.database.dao.ReminderDao
import com.lissu.data.database.entities.toEntity
import com.lissu.data.database.entities.toModel
import com.lissu.data.models.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(
  private val reminderDao: ReminderDao
) : ReminderRepository {

  override fun getReminders(): Flow<List<Reminder>> {
    return reminderDao.getAllReminders().map { reminders ->
      reminders.map { it.toModel() }
    }
  }

  override suspend fun addReminder(reminder: Reminder): Long {
    return reminderDao.insertReminder(reminder.toEntity())
  }

  override suspend fun deleteReminder(reminder: Reminder) {
    reminderDao.deleteReminder(reminder.toEntity())
  }

  override suspend fun updateReminder(reminder: Reminder) {
    reminderDao.updateReminder(reminder.toEntity())
  }
}