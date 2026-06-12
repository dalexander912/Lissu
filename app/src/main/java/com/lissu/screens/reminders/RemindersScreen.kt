package com.lissu.screens.reminders

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lissu.AppScaffold
import com.lissu.R
import com.lissu.Routes
import com.lissu.components.ReminderCard
import com.lissu.data.models.Reminder
import kotlinx.coroutines.launch

@Composable
fun RemindersScreen(
  viewModel: RemindersViewModel = viewModel(factory = RemindersViewModel.Factory),
  onBack: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToAddList: () -> Unit,
  onNavigateToMaps: () -> Unit,
  onNavigateToAccount: () -> Unit,
  context: Context
) {
  val reminders by viewModel.reminders.collectAsStateWithLifecycle()
  var reminderToDelete by rememberSaveable { mutableStateOf<Reminder?>(null) }

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val onDeletedSnackbar: () -> Unit = {
    scope.launch {
      snackbarHostState.showSnackbar("Recordatorio eliminado")
    }
  }

  AppScaffold(
    title = "Recordatorios",
    currentScreen = Routes.Reminders,
    onBack = onBack,
    snackbarHostState = snackbarHostState,
    onNavigateToHome = onNavigateToHome,
    onNavigateToAddList = onNavigateToAddList,
    onNavigateToMaps = onNavigateToMaps,
    onNavigateToAccount = onNavigateToAccount
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
      Spacer(Modifier.height(12.dp))

      Text("Mis recordatorios")
      Spacer(Modifier.height(16.dp))

      if(reminders.isEmpty()) {
        Column(
          modifier = Modifier.weight(1f).fillMaxWidth(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Image(
            painterResource(R.drawable.add_notif),
            contentDescription = "Recordatorios",
            colorFilter = ColorFilter.tint(Color.Gray),
            modifier = Modifier.size(80.dp)
          )
          Spacer(Modifier.height(32.dp))
          Text(
            text = "Tus recordatorios aparecerán aqui",
            color = Color.Gray,
            fontWeight = FontWeight.Bold
          )
        }
      } else {
        LazyColumn {
          items(reminders) { reminder ->
            ReminderCard(
              reminder = reminder,
              viewModel = viewModel,
              onDelete = { reminderToDelete = reminder },
              context = context
            )
            Spacer(Modifier.height(16.dp))
          }
        }
      }
    }
  }

  reminderToDelete?.let { reminder ->
    DeleteReminderAlert(
      reminder = reminder,
      onConfirm = {
        viewModel.deleteReminder(reminder, context)
        reminderToDelete = null
        onDeletedSnackbar()
      },
      onDismiss = { reminderToDelete = null }
    )
  }
}

@Composable
fun DeleteReminderAlert(
  reminder: Reminder,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    icon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
    title = { Text("Eliminar recordatorio") },
    text = { Text("Estas seguro/a que deseas eliminar el recordatorio para ${reminder.product}?") },
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(onConfirm) {
        Text(
          text = "Eliminar",
          color = MaterialTheme.colorScheme.onBackground,
          fontWeight = FontWeight.ExtraBold
        )
      }
    },
    dismissButton = {
      TextButton(onDismiss) {
        Text(
          text = "Cancelar",
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    }
  )
}