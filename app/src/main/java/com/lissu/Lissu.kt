package com.lissu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Lissu(
  modifier: Modifier,
  splashScreen: SplashScreen,
  context: Context,
  authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
  val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

  when (isLoggedIn) {
    null -> splashScreen.setKeepOnScreenCondition{ isLoggedIn != null }
    true -> MainNavigation(
      userName = authViewModel.userName.collectAsState().value,
      onLogout = { authViewModel.logout() },
      context = context
    )
    false -> SessionNavigation()
  }
}