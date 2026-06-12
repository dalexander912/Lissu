package com.lissu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lissu.screens.account.AccountScreen
import com.lissu.screens.home.HomeScreen
import com.lissu.screens.list.AddListScreen
import com.lissu.screens.login.LoginScreen
import com.lissu.screens.register.RegisterScreen

@Composable
fun Lissu(modifier: Modifier = Modifier) {
  val backStack = rememberNavBackStack(Routes.Home)
  var isLoggedIn by remember { mutableStateOf(false) }

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
          onNavigateToAccount = { backStack.add(Routes.Account) }
        )
      }
      entry<Routes.Login> {
        LoginScreen(
          onBack = { backStack.removeLastOrNull() },
          onNavigateToRegister = { backStack.add(Routes.Register) },
          onLoginSuccess = {
            isLoggedIn = true
            backStack.add(Routes.Account)
          },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) }
        )
      }
      entry<Routes.Home> {
        HomeScreen(
          onNavigateToHome = { },
          onNavigateToAddList = { id -> backStack.add(Routes.AddList(id)) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) }
        )
      }
      entry<Routes.AddList> { key ->
        AddListScreen(
          listId = key.id,
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { backStack.add(Routes.Account) }
        )
      }
      entry<Routes.Account> {
        AccountScreen(
          isLoggedIn = isLoggedIn,
          onLogout = { isLoggedIn = false },
          onBack = { backStack.removeLastOrNull() },
          onNavigateToHome = { backStack.add(Routes.Home) },
          onNavigateToAddList = { backStack.add(Routes.AddList()) },
          onNavigateToMaps = { backStack.add(Routes.Maps) },
          onNavigateToAccount = { },
          onNavigateToLogin = { backStack.add(Routes.Login) },
          onNavigateToRegister = { backStack.add(Routes.Register) }
        )
      }
      entry<Routes.Maps> {  }
    }
  )
}
