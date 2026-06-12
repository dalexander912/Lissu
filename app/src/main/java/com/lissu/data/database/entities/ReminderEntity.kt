package com.lissu.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lissu.data.models.Reminder

@Entity(tableName = "reminders")
data class ReminderEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val product: String,
  val intervalDays: Int,
  val isEnabled: Boolean = true
)

fun ReminderEntity.toModel(): Reminder {
  return Reminder(
    id = id,
    product = product,
    intervalDays = intervalDays,
    isEnabled = isEnabled
  )
}

fun Reminder.toEntity(): ReminderEntity {
  return ReminderEntity(
    id = id,
    product = product,
    intervalDays = intervalDays,
    isEnabled = isEnabled
  )
}