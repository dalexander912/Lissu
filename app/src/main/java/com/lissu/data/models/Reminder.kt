package com.lissu.models

data class Reminder(
  val id: Int, //Random.Default.nextInt()
  val product: String,
  val intervalDays: Int
)