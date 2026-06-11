package com.lissu.screens.reminders

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lissu.AppScaffold
import com.lissu.Routes
import com.lissu.components.ReminderCard
import com.lissu.data.dummy.dummyReminders

@Composable
fun RemindersScreen(
  viewModel: RemindersViewModel = viewModel(),
  onBack: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToAddList: () -> Unit,
  onNavigateToMaps: () -> Unit,
  onNavigateToAccount: () -> Unit,
  context: Context
) {
  // Usando recordatorios dummy por ahora para testing
  val reminders = dummyReminders

  AppScaffold(
    title = "Recordatorios",
    currentScreen = Routes.Reminders,
    onBack = onBack,
    onNavigateToHome = onNavigateToHome,
    onNavigateToAddList = onNavigateToAddList,
    onNavigateToMaps = onNavigateToMaps,
    onNavigateToAccount = onNavigateToAccount
  ) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding)
        .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
      Spacer(Modifier.height(12.dp))

      Text("Mis recordatorios")
      Spacer(Modifier.height(16.dp))

      LazyColumn() {
        items(reminders) { reminder ->
          ReminderCard(
            reminder,
            {  },
            context
          )
          Spacer(Modifier.height(16.dp))
        }
      }
    }
  }
}