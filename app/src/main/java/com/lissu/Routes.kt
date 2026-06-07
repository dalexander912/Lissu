package com.lissu

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {
  @Serializable
  data object Login : Routes()

  @Serializable
  data object Register : Routes()

  @Serializable
  data object Home : Routes()

  @Serializable
  data object AddList : Routes()

  @Serializable
  data object AddReminder : Routes()

  @Serializable
  data object Reminders : Routes()

  @Serializable
  data object Scanner : Routes()

  @Serializable
  data object Maps : Routes()

  @Serializable
  data object Account : Routes()
}