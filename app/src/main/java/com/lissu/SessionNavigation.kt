package com.lissu

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lissu.screens.login.LoginScreen
import com.lissu.screens.register.RegisterScreen

@Composable
fun SessionNavigation() {
  val backStack = rememberNavBackStack(Routes.Login)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Routes.Register> {
        RegisterScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToLogin = { backStack.add(Routes.Login) }
        )
      }
      entry<Routes.Login> {
        LoginScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToRegister = { backStack.add(Routes.Register) }
        )
      }
    }
  )
}