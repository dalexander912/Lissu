package com.lissu.screens.reminders

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.lissu.LissuApplication
import com.lissu.data.models.Reminder
import com.lissu.data.repositories.ReminderRepository
import com.lissu.notifications.cancelReminder
import com.lissu.notifications.scheduleReminder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RemindersViewModel(
  private val reminderRepository: ReminderRepository
) : ViewModel() {

  val reminders: StateFlow<List<Reminder>> =
    reminderRepository.getReminders()
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
      )

  fun deleteReminder(
    reminder: Reminder,
    context: Context
  ) {
    viewModelScope.launch {
      if(reminder.isEnabled) {
        cancelReminder(reminder.id, context)
      }

      reminderRepository.deleteReminder(reminder)
    }
  }

  fun updateReminder(
    reminder: Reminder,
    isEnabled: Boolean,
    context: Context
  ) {
    viewModelScope.launch {
      val updatedReminder = reminder.copy(isEnabled = isEnabled)
      reminderRepository.updateReminder(updatedReminder)

      if(isEnabled) {
        scheduleReminder(updatedReminder, context)
      } else {
        cancelReminder(updatedReminder.id, context)
      }
    }
  }

  companion object {
    val Factory = viewModelFactory {
      initializer {
        val app = this[APPLICATION_KEY] as LissuApplication
        RemindersViewModel(app.appProvider.provideReminderRepository())
      }
    }
  }
}