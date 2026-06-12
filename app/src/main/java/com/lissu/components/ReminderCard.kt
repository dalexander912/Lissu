package com.lissu.components

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lissu.data.models.Reminder
import com.lissu.screens.reminders.RemindersViewModel
import com.lissu.ui.theme.Lissu_DarkPurple
import com.lissu.ui.theme.Lissu_LightPurple

@Composable
fun ReminderCard(
  reminder: Reminder,
  viewModel: RemindersViewModel,
  onDelete: () -> Unit,
  context: Context
) {
  val isDark = isSystemInDarkTheme()

  val days = reminder.intervalDays

  Card(
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.height(IntrinsicSize.Min),
      verticalAlignment = Alignment.CenterVertically
    ) {

      Column(modifier = Modifier.weight(1f).padding(12.dp)) {
        Text(reminder.product, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
          text = if(days == 1) "Cada día" else "Cada $days días",
          fontSize = 12.sp
        )
      }

      VerticalDivider(thickness = 2.dp)

      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          Icons.Outlined.Notifications,
          contentDescription = null,
          tint = if(isDark) Color.White else Color.Black
        )
        Spacer(Modifier.width(8.dp))
        Switch(
          checked = reminder.isEnabled,
          onCheckedChange = { isChecked ->
            viewModel.updateReminder(reminder, isChecked, context)
          },
          colors = SwitchDefaults.colors(
            checkedThumbColor = if(isDark) Lissu_DarkPurple else Lissu_LightPurple,
            checkedTrackColor = if(isDark) Lissu_LightPurple else Lissu_DarkPurple
          )
        )
      }

      VerticalDivider(thickness = 2.dp)

      IconButton(
        onClick = onDelete,
        modifier = Modifier.padding(vertical = 12.dp)
      ) {
        Icon(
          Icons.Outlined.Delete,
          contentDescription = null,
          tint = if(isDark) Color.White else Color.Black
        )
      }
    }
  }
}