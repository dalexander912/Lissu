package com.lissu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lissu.ui.theme.LissuTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    installSplashScreen()
    setContent {
      LissuTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Lissu(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@Composable
fun Lissu(modifier: Modifier = Modifier) {
  AppScaffold(title = "Usuario 1") {
    Text("Test")
  }
}