package com.lissu.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.lissu.data.models.ShoppingList
import com.lissu.ui.theme.Lissu_Purple
import com.lissu.ui.theme.Lissu_Purple2

@Composable
fun HomeScreen(
  viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
  onNavigateToHome: () -> Unit,
  onNavigateToAddList: (String?) -> Unit,
  onNavigateToMaps: () -> Unit,
  onNavigateToAccount: () -> Unit,
  onNavigateToAddReminder: () -> Unit,
  onNavigateToReminders: () -> Unit,
  onNavigateToScanner: () -> Unit,
) {
  val isDark = isSystemInDarkTheme()
  val shoppingLists by viewModel.shoppingLists.collectAsState()

  AppScaffold(
    title = "Usuario1",
    currentScreen = Routes.Home,
    onNavigateToHome = onNavigateToHome,
    onNavigateToAddList = { onNavigateToAddList(null) },
    onNavigateToMaps = onNavigateToMaps,
    onNavigateToAccount = onNavigateToAccount,
    onNavigateToReminders = onNavigateToReminders
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
    ) {
      // LISTAS //
      Text(
        "Tus listas",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
      )

      if (shoppingLists.isEmpty()) {
        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Image(
            painterResource(R.drawable.add_list),
            contentDescription = "Listas",
            colorFilter = ColorFilter.tint(if (isDark) Color.DarkGray else Color.LightGray),
            modifier = Modifier.size(80.dp)
          )
          Spacer(Modifier.height(32.dp))
          Text(
            text = "Tus listas de compras aparecerán aqui",
            color = if (isDark) Color.DarkGray else Color.LightGray,
            fontWeight = FontWeight.Bold
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(8.dp)
        ) {
          items(shoppingLists) { list ->
            ShoppingListCard(
              list = list,
              onClick = { onNavigateToAddList(list.id) },
              onDelete = { viewModel.deleteList(list) }
            )
          }
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
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToAddReminder() },
          color = if (isDark) Lissu_Purple else Lissu_Purple2,
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
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToAddList(null) },
          color = if (isDark) Lissu_Purple else Lissu_Purple2,
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
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToScanner() },
        color = if (isDark) Lissu_Purple else Lissu_Purple2,
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

@Composable
fun ShoppingListCard(
  list: ShoppingList,
  onClick: () -> Unit,
  onDelete: () -> Unit
) {
  val isDark = isSystemInDarkTheme()
  val containerColor = if (isDark) Color(0xFF2D1F35) else Color(0xFFE1D5E7)
  val contentColor = if (isDark) Color.White else Color.Black
  var showMenu by remember { mutableStateOf(false) }

  Surface(
    color = containerColor,
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = list.name,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = contentColor
        )
        Text(
          text = "${list.items.size} artículos",
          fontSize = 12.sp,
          color = contentColor.copy(alpha = 0.7f),
          fontWeight = FontWeight.Medium
        )
      }

      Column(
        modifier = Modifier
          .weight(2.5f)
          .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center
      ) {
        list.items.take(4).forEach { item ->
          Text(
            text = "• ${item.name}",
            fontSize = 13.sp,
            color = contentColor.copy(alpha = 0.8f),
            maxLines = 1,
            lineHeight = 16.sp
          )
        }
        if (list.items.size > 4) {
          Text(
            text = "• ...",
            fontSize = 13.sp,
            color = contentColor.copy(alpha = 0.8f),
            lineHeight = 16.sp
          )
        }
      }

      Box {
        IconButton(onClick = { showMenu = true }) {
          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Opciones",
            tint = contentColor
          )
        }
        DropdownMenu(
          expanded = showMenu,
          onDismissRequest = { showMenu = false }
        ) {
          DropdownMenuItem(
            text = { Text("Editar") },
            onClick = {
              showMenu = false
              onClick()
            },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
          )
          DropdownMenuItem(
            text = { Text("Eliminar") },
            onClick = {
              showMenu = false
              onDelete()
            },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
          )
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
    onNavigateToAddReminder = {},
    onNavigateToReminders = {},
    onNavigateToScanner = {}
  )
}