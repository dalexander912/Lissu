package com.lissu.screens.addReminder

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lissu.AppScaffold
import com.lissu.R
import com.lissu.Routes
import com.lissu.data.models.Reminder
import com.lissu.ui.theme.Lissu_LightPurple
import com.lissu.ui.theme.Lissu_Purple2
import kotlinx.coroutines.launch

@Composable
fun AddReminderScreen(
  viewModel: AddReminderViewModel = viewModel(factory = AddReminderViewModel.Factory),
  onBack: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToAddList: () -> Unit,
  onNavigateToMaps: () -> Unit,
  onNavigateToAccount: () -> Unit,
  onNavigateToReminders: () -> Unit,
  context: Context
) {
  val isDark = isSystemInDarkTheme()

  val reminders by viewModel.reminders.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val onCreatedSnackbar: () -> Unit = {
    scope.launch {
      snackbarHostState.showSnackbar("Recordatorio creado exitosamente!")
    }
  }

  var product by rememberSaveable { mutableStateOf("") }
  var daysInput by rememberSaveable { mutableStateOf("") }
  val days = daysInput.toIntOrNull() ?: 0
  val isInputValid = ( product.isNotBlank() && days > 0 )

  fun createReminder() {
    viewModel.addReminder(
      reminder = Reminder(product = product, intervalDays = days),
      context = context
    )
    onCreatedSnackbar()
    product = ""
    daysInput = ""
  }

  AppScaffold(
    title = "Recordatorio",
    currentScreen = Routes.AddReminder,
    onBack = onBack,
    snackbarHostState = snackbarHostState,
    onNavigateToHome = onNavigateToHome,
    onNavigateToAddList = onNavigateToAddList,
    onNavigateToMaps = onNavigateToMaps,
    onNavigateToAccount = onNavigateToAccount,
    onNavigateToReminders = onNavigateToReminders
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .padding(vertical = 4.dp, horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Spacer(Modifier.height(12.dp))

      Text("Crear recordatorio")
      Spacer(Modifier.height(16.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Image(
            painterResource(R.drawable.add_notif),
            contentDescription = "Crear recordatorio",
            modifier = Modifier
              .align(Alignment.End)
              .size(40.dp)
          )

          Text(text = "Producto", fontSize = 12.sp)
          Spacer(Modifier.height(4.dp))
          OutlinedTextField(
            value = product,
            onValueChange = { product = it },
            placeholder = { Text("Ingresar nombre del producto") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedTextColor = Color.White,
              focusedTextColor = Color.White,
              unfocusedBorderColor = Lissu_Purple2,
              focusedBorderColor = Color.White,
              unfocusedPlaceholderColor = Lissu_Purple2,
              focusedPlaceholderColor = Color.Transparent,
              cursorColor = Color.White,
              selectionColors = TextSelectionColors(Color.White, Lissu_Purple2)
            )
          )

          Spacer(Modifier.height(16.dp))

          Text(text = "Lapso en días", fontSize = 12.sp)
          Spacer(Modifier.height(4.dp))
          OutlinedTextField(
            value = daysInput,
            onValueChange = { daysInput = it },
            placeholder = { Text("Ingresar lapso de tiempo en días") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
              keyboardType = KeyboardType.Number
            ),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedTextColor = Color.White,
              focusedTextColor = Color.White,
              unfocusedBorderColor = Lissu_Purple2,
              focusedBorderColor = Color.White,
              unfocusedPlaceholderColor = Lissu_Purple2,
              focusedPlaceholderColor = Color.Transparent,
              cursorColor = Color.White,
              selectionColors = TextSelectionColors(Color.White, Lissu_Purple2)
            )
          )

          Spacer(Modifier.height(24.dp))

          Text(
            text = "Lissu te recordará periodicamente que debes comprar este producto luego del lapso de tiempo establecido.",
            fontSize = 12.sp,
            color = Lissu_LightPurple,
            lineHeight = 16.sp
          )
          Spacer(Modifier.height(8.dp))
          Text(
            text = "Una vez creado el recordatorio podrás desactivar o activar las notificaciones en cualquier momento.",
            fontSize = 12.sp,
            color = Lissu_LightPurple,
            lineHeight = 16.sp
          )

          Spacer(Modifier.height(24.dp))

          Button(
            onClick = { createReminder() },
            modifier = Modifier.fillMaxWidth(),
            enabled = isInputValid,
            colors = ButtonDefaults.buttonColors(
              containerColor = Lissu_Purple2,
              disabledContentColor = if(isDark) Color.Unspecified else Lissu_Purple2
            )
          ) { Text(text = "Agregar", fontWeight = FontWeight.Bold) }
        }
      }

      HorizontalDivider(modifier = Modifier.padding(vertical = 32.dp))

      Text(
        text = "Recordatorios creados",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
      )
      Spacer(Modifier.height(16.dp))

      if(reminders.isEmpty()) {
        Text(
          text = "No hay recordatorios creados actualmente",
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          color = Color.Gray
        )
      } else {
        for(reminder in reminders) {

          val days = reminder.intervalDays

          Card(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)) {
              Text(
                text = reminder.product,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Spacer(Modifier.width(16.dp))
              Text(
                text = if(days == 1) "Cada día" else "Cada $days días",
                fontSize = 12.sp,
                color = Color.Gray
              )
            }
          }
          Spacer(Modifier.height(8.dp))
        }
      }
    }
  }
}