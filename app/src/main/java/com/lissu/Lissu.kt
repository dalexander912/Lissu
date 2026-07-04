package com.lissu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lissu.screens.account.AccountScreen
import com.lissu.screens.addReminder.AddReminderScreen
import com.lissu.screens.home.HomeScreen
import com.lissu.screens.list.AddListScreen
import com.lissu.screens.login.LoginScreen
import com.lissu.screens.register.RegisterScreen
import com.lissu.screens.reminders.RemindersScreen
import com.lissu.screens.scanner.ScannerScreen

@Composable
fun Lissu(
  modifier: Modifier = Modifier,
  context: Context,
  authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
  val backStack = rememberNavBackStack(Routes.Home)
  val username by authViewModel.userName.collectAsState()

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Routes.Register> {
        RegisterScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToLogin = { backStack.add(Routes.Login) },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) }
        )
      }
      entry<Routes.Login> {
        LoginScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToRegister = { backStack.add(Routes.Register) },
          onLoginSuccess = {
            // backStack.add(Routes.Account)
          },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) }
        )
      }
      entry<Routes.Home> {
        HomeScreen(
          onNavigateToHome = { },
          onNavigateToAddList = { id -> backStack.add(Routes.AddList(id)) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToAddReminder = { backStack.add(Routes.AddReminder) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) },
          onNavigateToScanner = { backStack.add(Routes.Scanner) },
          username = username?: "Invitado"
        )
      }
      entry<Routes.AddList> { key ->
        AddListScreen(
          listId = key.id,
          onBack = { backStack.removeLastOrNull() },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) },
          username = username?: "Invitado"
        )
      }
      entry<Routes.AddReminder> {
        AddReminderScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) },
          context = context
        )
      }
      entry<Routes.Reminders> {
        RemindersScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          context = context
        )
      }
      entry<Routes.Scanner> {
        ScannerScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) }
        )
      }
      entry<Routes.Maps> {
        
      }
      entry<Routes.Account> {
        AccountScreen(
          onLogout = { authViewModel.logout() },
          onBack = { backStack.removeLastOrNull() },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { },
          onNavigateToLogin = { backStack.add(Routes.Login) },
          onNavigateToRegister = { backStack.add(Routes.Register) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) }
        )
      }
    }
  )
}
