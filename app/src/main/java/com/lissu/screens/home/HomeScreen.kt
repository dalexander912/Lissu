package com.lissu.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lissu.AppScaffold
import com.lissu.R
import com.lissu.Routes
import com.lissu.ui.theme.Lissu_Purple
import com.lissu.ui.theme.Lissu_Purple2

@Composable
fun HomeScreen(
  viewModel: HomeViewModel = viewModel(),
  onNavigateToHome: () -> Unit,
  onNavigateToAddList: () -> Unit,
  onNavigateToMaps: () -> Unit,
  onNavigateToAccount: () -> Unit,
  onNavigateToScanner: () -> Unit,
) {
  val isDark = isSystemInDarkTheme()
  val shoppingLists = emptyList<Any>()

  AppScaffold(
    title = "Usuario1",
    currentScreen = Routes.Home,
    onNavigateToHome = onNavigateToHome,
    onNavigateToAddList = onNavigateToAddList,
    onNavigateToMaps = onNavigateToMaps,
    onNavigateToAccount = onNavigateToAccount
  ) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding)
        .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
    ) {
      // LISTAS //

      Text("Mis listas")
      if(shoppingLists.isEmpty()) {
        Column(
          modifier = Modifier.weight(1f).fillMaxWidth(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Image(
            painterResource(R.drawable.add_list),
            contentDescription = "Listas",
            colorFilter = ColorFilter.tint(if(isDark) Color.DarkGray else Color.LightGray),
            modifier = Modifier.size(80.dp)
          )
          Spacer(Modifier.height(32.dp))
          Text(
            text = "Tus listas de compras aparecerán aqui",
            color = if(isDark) Color.DarkGray else Color.LightGray,
            fontWeight = FontWeight.Bold
          )
        }
      } else {
        LazyColumn(modifier = Modifier.weight(1f)) {

        }
      }

      HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

      // BOTONES //

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Agregar recordatorio //
        Surface(
          modifier = Modifier.weight(1f)
            .clickable {  },
          color = if(isDark) Lissu_Purple else Lissu_Purple2,
          contentColor = Color.White,
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Image(
              painterResource(R.drawable.add_notif),
              contentDescription = "Recordatorio",
              modifier = Modifier.size(32.dp)
            )
            Text("Agregar recordatorio", fontSize = 12.sp)
          }
        }
        // Agregar producto //
        Surface(
          modifier = Modifier.weight(1f)
            .clickable {  },
          color = if(isDark) Lissu_Purple else Lissu_Purple2,
          contentColor = Color.White,
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Image(
              painterResource(R.drawable.add_cart),
              contentDescription = "Producto",
              modifier = Modifier.size(32.dp)
            )
            Text("Agregar producto", fontSize = 12.sp)
          }
        }
      }
      Spacer(Modifier.height(16.dp))

      // Escanear codigo de barras //
      Surface(
        modifier = Modifier.fillMaxWidth()
          .clickable { onNavigateToScanner() },
        color = if(isDark) Lissu_Purple else Lissu_Purple2,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(
          modifier = Modifier.padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Image(
            painterResource(R.drawable.barcode),
            contentDescription = "Escanear",
            modifier = Modifier.size(32.dp)
          )
          Spacer(Modifier.width(32.dp))
          Text("Escanear código de barras", fontSize = 12.sp)
        }
      }
    }
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun HomePreview() {
  HomeScreen(
    onNavigateToHome = {},
    onNavigateToAddList = {},
    onNavigateToMaps = {},
    onNavigateToAccount = {},
    onNavigateToScanner = {}
  )
}
