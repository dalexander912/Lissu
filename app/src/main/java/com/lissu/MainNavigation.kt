package com.lissu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lissu.screens.account.AccountScreen
import com.lissu.screens.addReminder.AddReminderScreen
import com.lissu.screens.home.HomeScreen
import com.lissu.screens.list.AddListScreen
import com.lissu.screens.reminders.RemindersScreen
import com.lissu.screens.scanner.ScannerScreen

@Composable
fun MainNavigation(
  userName: String?,
  onLogout: () -> Unit,
  context: Context
) {
  val backStack = rememberNavBackStack(Routes.Home)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Routes.Home> {
        HomeScreen(
          onNavigateToHome = { },
          onNavigateToAddList = { id -> backStack.add(Routes.AddList(id)) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) },
          onNavigateToAddReminder = { backStack.add(Routes.AddReminder) },
          onNavigateToReminders = { backStack.add(Routes.Reminders) },
          onNavigateToScanner = { backStack.add(Routes.Scanner) },
          username = userName?: "Usuario"
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
          username = userName?: "Usuario"
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
          onLogout = onLogout,
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