package com.lissu.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.lissu.R
import com.lissu.ui.theme.Lissu_Purple

class NotificationService(
  private val context: Context
) {
  private val notificationManager = context.getSystemService(NotificationManager::class.java)
  fun showNotification(
    reminderId: Int,
    product: String,
    days: String
  ) {
    val messageStart = if(days == "1") "Cada dia" else "Cada $days dias"
    val message = "$messageStart: Recuerda agregar $product a tu próxima lista de compras!"

    val notification = NotificationCompat.Builder(context,"lissu_channel")
      .setContentTitle("Recordatorio")
      .setContentText(message)
      .setStyle(NotificationCompat.BigTextStyle()
        .bigText(message))
      .setSmallIcon(R.drawable.ic_notification)
      .setColor(ContextCompat.getColor(context, R.color.Lissu_Purple))
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true)

    notificationManager.notify(reminderId, notification.build())
  }
}