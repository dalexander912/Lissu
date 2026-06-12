package com.lissu

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
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
  onBack: (() -> Unit)? = null,
  navigationIcon: @Composable (() -> Unit)? = null,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  showTopBar: Boolean = true,
  showBottomBar: Boolean = true,

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
      if (showTopBar && title.isNotEmpty()) {
        TopAppBar(
          colors = TopAppBarDefaults.topAppBarColors(
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
              Spacer(Modifier.width(8.dp))
              Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
          },
          navigationIcon = {
            if (navigationIcon != null) {
              navigationIcon()
            } else if (onBack != null) {
              IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
              }
            }
          },
          actions = {
            Box {
              IconButton(onClick = { }) {
                Icon(Icons.Outlined.Notifications, "Notificaciones", tint = Color.White)
              }
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .align(Alignment.TopEnd)
                  .offset(x = (-8).dp, y = 8.dp)
                  .background(Color.White, CircleShape)
              )
            }
            IconButton(onClick = { }) {
              Icon(Icons.Outlined.Settings, "Configuración", tint = Color.White)
            }
          }
        )
      }
    },
    bottomBar = {
      if (showBottomBar) {
        BottomAppBar(
          containerColor = if(isDark) Lissu_DarkPurple else Lissu_LightPurple,
          contentColor = if(isDark) Color.White else Color.Black,
          actions = {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              BottomBarButton(onNavigateToHome, R.drawable.home, "Inicio", Routes.Home, currentScreen, isDark)
              BottomBarButton(onNavigateToAddList, R.drawable.add_list, "Agregar", Routes.AddList, currentScreen, isDark)
              BottomBarButton(onNavigateToMaps, R.drawable.store, "Buscar", Routes.Maps, currentScreen, isDark)
              BottomBarButton(onNavigateToAccount, R.drawable.person, "Cuenta", Routes.Account, currentScreen, isDark)
            }
          }
        )
      }
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
