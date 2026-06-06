package com.lissu

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lissu.ui.theme.Lissu_DarkPurple
import com.lissu.ui.theme.Lissu_LightPurple
import com.lissu.ui.theme.Lissu_Purple
import com.lissu.ui.theme.Lissu_Purple2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
  modifier: Modifier = Modifier,
  title: String = "",
  currentScreen: Routes,
  navigationIcon: @Composable (() -> Unit)? = null,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },

  onNavigateToHome: () -> Unit = {},
  onNavigateToAddList: () -> Unit = {},
  onNavigateToMaps: () -> Unit = {},
  onNavigateToAccount: () -> Unit = {},

  content: @Composable (PaddingValues) -> Unit


) {
  val isDark = isSystemInDarkTheme()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      if (title.isNotEmpty()) {
        TopAppBar(
          colors = topAppBarColors(
            containerColor = Lissu_Purple,
            titleContentColor = Color.White,
          ),
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Image(
                painterResource(R.drawable.logo),
                contentDescription = "Lissu",
                modifier = Modifier.size(56.dp)
              )
              Spacer(Modifier.width(12.dp))
              Text(title, fontWeight = FontWeight.Bold)
            }
          },
          navigationIcon = { navigationIcon?.invoke() },
          actions = {
            IconButton(onClick = {  }) {
              Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Recordatorios",
                tint = Color.White
              )
            }
          }
        )
      }
    },
    bottomBar = {
      BottomAppBar(
        containerColor = if(isDark) Lissu_DarkPurple else Lissu_LightPurple,
        contentColor = if(isDark) Color.White else Color.Black,
        actions = {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            BottomBarButton(onNavigateToHome,
              R.drawable.home, "Inicio", Routes.Home, currentScreen, isDark)
            BottomBarButton(onNavigateToAddList,
              R.drawable.add_list, "Agregar", Routes.AddList, currentScreen, isDark)
            BottomBarButton(onNavigateToMaps,
              R.drawable.store, "Buscar", Routes.Maps, currentScreen, isDark)
            BottomBarButton(onNavigateToAccount,
              R.drawable.person, "Cuenta", Routes.Account, currentScreen, isDark)
          }
        }
      )
    }
  ) { innerPadding ->
    content(innerPadding)
  }
}

@Composable
fun BottomBarButton(
  onClick: () -> Unit,
  imageId: Int,
  text: String,
  screen: Routes,
  currentScreen: Routes,
  isDark: Boolean
) {
  Card(
    modifier = Modifier.clickable { onClick() }
      .width(70.dp)
      .drawBehind {
        if(currentScreen == screen) {
          drawRoundRect(
            color = if(isDark) Lissu_Purple2 else Lissu_DarkPurple,
            alpha = if(isDark) 0.3f else 0.1f,
            cornerRadius = CornerRadius(8.dp.toPx())
          )
        }
      },
    shape = RoundedCornerShape(4.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(4.dp),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Image(
        painterResource(imageId),
        contentDescription = text,
        colorFilter = if(isDark) ColorFilter.tint(Color.White) else null,
        modifier = Modifier.size(24.dp)
      )
      Text(text, fontSize = 12.sp)
    }
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode", showBackground = true)
@Composable
fun AppScaffoldPreview() {
  AppScaffold(title = "Usuario1", currentScreen = Routes.Home) { innerPadding ->
    Text("Test", modifier = Modifier.padding(innerPadding))
  }
}