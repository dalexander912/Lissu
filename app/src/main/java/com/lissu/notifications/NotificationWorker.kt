package com.lissu.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lissu.models.Reminder
import java.util.concurrent.TimeUnit

class NotificationWorker(
  context: Context,
  params: WorkerParameters
) : Worker(context, params) {

  override fun doWork(): Result {
    val id = inputData.getInt("id", 1)
    val product = inputData.getString("product") ?: "producto"
    val days = inputData.getString("intervalDays") ?: "n"

    val notificationService = NotificationService(applicationContext)
    notificationService.showNotification(id, product, days)
    return Result.success()
  }
}

fun scheduleReminder(
  reminder: Reminder,
  context: Context
) {
  val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
    reminder.intervalDays.toLong(),
    TimeUnit.DAYS
  )
    .setInputData(
      workDataOf(
        "id" to reminder.id,
        "product" to reminder.product,
        "intervalDays" to reminder.intervalDays
      )
    )
    .build()

  WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "reminder_${reminder.id}",
    ExistingPeriodicWorkPolicy.UPDATE,
    workRequest
  )
}

fun cancelReminder(
  reminderId: Int,
  context: Context
) {
  WorkManager.getInstance(context)
    .cancelUniqueWork("reminder_$reminderId")
}