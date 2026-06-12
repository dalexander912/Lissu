package com.lissu.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lissu.AppScaffold
import com.lissu.Routes
import com.lissu.data.Item
import com.lissu.ui.theme.Lissu_Purple
import com.lissu.ui.theme.Lissu_Purple2
import com.lissu.ui.theme.PurpleGrey40

@Composable
fun AddListScreen(
    viewModel: AddListViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToAddList: () -> Unit,
    onNavigateToMaps: () -> Unit,
    onNavigateToAccount: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }

    val containerColor = if (isDark) Color(0xFF2D1F35) else Color(0xFFE1D5E7)
    val borderColor = if (isDark) Lissu_Purple2 else Color(0xFF9E86B9)
    val titleColor = if (isDark) Color.White else Color.Black

    AppScaffold(
        title = "Usuario1",
        currentScreen = Routes.AddList,
        onNavigateToHome = onNavigateToHome,
        onNavigateToAddList = onNavigateToAddList,
        onNavigateToMaps = onNavigateToMaps,
        onNavigateToAccount = onNavigateToAccount
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(if (isDark) Color(0xFF1A1A1A) else Color.White)
                .padding(12.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
                color = containerColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = viewModel.listName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(viewModel.items) { item ->
                            ShoppingItemRow(
                                item = item,
                                onToggle = { viewModel.toggleItem(item.id) },
                                onRemove = { viewModel.removeItem(item.id) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { onNavigateToHome() },
                            modifier = Modifier.weight(0.25f).height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Lissu_Purple),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {

                            Icon(Icons.Default.Save, "Guardar", modifier = Modifier.size(32.dp))

                        }

                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.weight(0.75f).height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Lissu_Purple2),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.Add, "Agregar", modifier = Modifier.size(32.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Añadir", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Agregar producto") },
                    text = {
                        OutlinedTextField(
                            value = newItemName,
                            onValueChange = { newItemName = it },
                            label = { Text("Nombre del artículo") },
                            singleLine = true
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (newItemName.isNotBlank()) {
                                viewModel.addItem(newItemName)
                                newItemName = ""
                                showDialog = false
                            }
                        }) { Text("Guardar") }
                    }
                )
            }
        }
    }
}

@Composable
fun ShoppingItemRow(
    item: Item,
    onToggle: () -> Unit,
    onRemove: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val itemBgColor = if (item.isChecked) {
        if (isDark) PurpleGrey40.copy(alpha = 0.8f) else Color(0xFFC7B8D1)
    } else {
        if (isDark) Color(0xFF4A375A) else Color(0xFFD0C3D8)
    }

    val textColor = if (isDark) Color.White else Color.Black
    val checkboxColor = if (isDark) Color.White else Color.Black

    Surface(
        color = itemBgColor,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = checkboxColor,
                    checkmarkColor = if (isDark) Color.Black else Color.White,
                    uncheckedColor = checkboxColor
                )
            )
            Text(
                text = item.name,
                modifier = Modifier.weight(1f).clickable { onToggle() },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Close, 
                    "Borrar", 
                    tint = if (isDark) Color.LightGray else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
