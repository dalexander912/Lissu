package com.lissu

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.lissu.notifications.createNotificationChannel
import com.lissu.ui.theme.LissuTheme

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalPermissionsApi::class)
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)
    createNotificationChannel(this)
    enableEdgeToEdge()
    setContent {
      LissuTheme {
        val postNotificationPermission =
          rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(key1 = true) {
          if(!postNotificationPermission.status.isGranted){
            postNotificationPermission.launchPermissionRequest()
          }
        }
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Lissu(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}