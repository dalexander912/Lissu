package com.lissu

import android.content.Context
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
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
  val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

  when (isLoggedIn) {
    true -> MainNavigation(
      userName = authViewModel.userName.collectAsState().value,
      onLogout = { authViewModel.logout() },
      context = context
    )
    else -> SessionNavigation()
  }
}