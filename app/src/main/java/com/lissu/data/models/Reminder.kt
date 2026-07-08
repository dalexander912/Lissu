package com.lissu.data.models

data class Reminder(
  val id: Int = 0,
  val product: String,
  val intervalDays: Int,
  val isEnabled: Boolean = true
)