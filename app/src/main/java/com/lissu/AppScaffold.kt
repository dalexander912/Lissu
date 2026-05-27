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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
  modifier: Modifier = Modifier,
  title: String = "",
  navigationIcon: @Composable (() -> Unit)? = null,
  onFabClick: (() -> Unit)? = null,
  fabIcon: @Composable (() -> Unit)? = null,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  content: @Composable (PaddingValues) -> Unit
) {
  val PURPLE = colorResource(R.color.Lissu_Purple)
  val LIGHT_PURPLE = colorResource(R.color.Lissu_LightPurple)
  val DARK_PURPLE = colorResource(R.color.Lissu_DarkPurple)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      if (title.isNotEmpty()) {
        TopAppBar(
          colors = topAppBarColors(
            containerColor = PURPLE,
            titleContentColor = Color.White,
          ),
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Image(
                painterResource(R.drawable.logo),
                contentDescription = "",
                modifier = Modifier.size(64.dp)
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
                contentDescription = "Notificaciones",
                tint = Color.White
              )
            }
          }
        )
      }
    },
    bottomBar = {
      BottomAppBar(
        containerColor = if(isSystemInDarkTheme()) DARK_PURPLE else LIGHT_PURPLE,
        contentColor = if(isSystemInDarkTheme()) Color.White else Color.Black,
        actions = {
          Row(
            modifier = Modifier.fillMaxSize().padding(top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            BottomBarButton({  }, R.drawable.home, "Inicio")
            BottomBarButton({  }, R.drawable.add_list, "Agregar")
            BottomBarButton({  }, R.drawable.store, "Buscar")
            BottomBarButton({  }, R.drawable.person, "Cuenta")
          }
        }
      )
    },
    floatingActionButton = {
      if (onFabClick != null) {
        FloatingActionButton(onClick = onFabClick) {
          if (fabIcon != null) fabIcon()
          else Icon(Icons.Default.Add, contentDescription = "Add")
        }
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
  text: String
) {
  Column(
    modifier = Modifier.clickable( onClick = onClick ),
    verticalArrangement = Arrangement.SpaceEvenly,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painterResource(imageId),
      contentDescription = text,
      modifier = Modifier.size(28.dp),
      colorFilter =
        if(isSystemInDarkTheme()) ColorFilter.tint(Color.White) else null
    )
    Text(text, fontSize = 10.sp)
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")
@Composable
fun AppScaffoldPreview() {
  AppScaffold(title = "Usuario 1") {
    Text("Test")
  }
}