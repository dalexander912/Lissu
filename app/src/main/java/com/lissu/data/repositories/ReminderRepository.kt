package com.lissu.data.repositories

import com.lissu.data.models.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
  fun getReminders(): Flow<List<Reminder>>
  suspend fun addReminder(reminder: Reminder): Long
  suspend fun deleteReminder(reminder: Reminder)
  suspend fun updateReminder(reminder: Reminder)
}