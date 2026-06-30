package com.lissu.screens.addReminder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lissu.LissuApplication
import com.lissu.data.models.Reminder
import com.lissu.data.repositories.ReminderRepository
import com.lissu.notifications.scheduleReminder
import com.lissu.screens.reminders.RemindersViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddReminderViewModel(
  private val reminderRepository: ReminderRepository
) : ViewModel() {

  val reminders: StateFlow<List<Reminder>> =
    reminderRepository.getReminders()
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
      )

  fun addReminder(reminder: Reminder, context: Context) {
    viewModelScope.launch {
      // Se guarda el recordatorio y se toma el id autogenerado por Room
      val id = reminderRepository.addReminder(reminder)

      // Se programa el recordatorio con el id real generado por Room
      scheduleReminder(reminder.copy(id = id.toInt()), context)
    }
  }

  companion object {
    val Factory = viewModelFactory {
      initializer {
        val app = this[APPLICATION_KEY] as LissuApplication
        AddReminderViewModel(app.appProvider.provideReminderRepository())
      }
    }
  }
}