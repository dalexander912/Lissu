package com.lissu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lissu.screens.home.HomeScreen

@Composable
fun Lissu(modifier: Modifier = Modifier) {
  val backStack = rememberNavBackStack(Routes.Home)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Routes.Login> {

      }
      entry<Routes.Home> {
        HomeScreen()
      }
      entry<Routes.AddList> {

      }
      entry<Routes.AddReminder> {

      }
      entry<Routes.Reminders> {

      }
      entry<Routes.Scanner> {
        onBack = { backStack.removeLastOrNull() }
      }
      entry<Routes.Maps> {

      }
      entry<Routes.Account> {

      }
    }
  )
}