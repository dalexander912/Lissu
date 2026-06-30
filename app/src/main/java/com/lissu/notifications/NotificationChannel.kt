package com.lissu.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(context: Context) {

  val notificationChannel = NotificationChannel(
    "lissu_channel",
    "Lissu notifications",
    NotificationManager.IMPORTANCE_HIGH
  )
  val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
  notificationManager.createNotificationChannel(notificationChannel)
}