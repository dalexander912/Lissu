package com.lissu.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lissu.AppScaffold
import com.lissu.Routes
import com.lissu.data.models.Item
import com.lissu.ui.theme.Lissu_Purple
import com.lissu.ui.theme.Lissu_Purple2
import com.lissu.ui.theme.PurpleGrey40
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddListScreen(
    listId: String? = null,
    viewModel: AddListViewModel = viewModel(factory = AddListViewModel.Factory),
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAddList: () -> Unit,
    onNavigateToMaps: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToReminders: () -> Unit,
    username: String
) {
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }

    val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    LaunchedEffect(listId) {
        viewModel.loadList(listId)
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AddListViewModel.UiEvent.SaveSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "Lista guardada con éxito",
                        duration = SnackbarDuration.Short
                    )
                }
                is AddListViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val containerColor = if (isDark) Color(0xFF2D1F35) else Color(0xFFE1D5E7)
    val borderColor = if (isDark) Lissu_Purple2 else Color(0xFF9E86B9)
    val titleColor = if (isDark) Color.White else Color.Black

    AppScaffold(
        title = username,
        currentScreen = Routes.AddList(),
        onBack = onBack,
        snackbarHostState = snackbarHostState,
        onNavigateToHome = onNavigateToHome,
        onNavigateToAddList = onNavigateToAddList,
        onNavigateToMaps = onNavigateToMaps,
        onNavigateToAccount = onNavigateToAccount,
        onNavigateToReminders = onNavigateToReminders
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
                    .fillMaxSize(),
                    
                color = containerColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    TextField(
                        value = viewModel.listName,
                        onValueChange = { viewModel.updateListName(it) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = titleColor,
                            unfocusedTextColor = titleColor
                        ),
                        singleLine = true,
                        placeholder = {
                            Text(
                                "Nombre lista",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = titleColor.copy(alpha = 0.5f)
                            )
                        }
                    )

                    Text(
                        "Día asignado:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(daysOfWeek) { day ->
                            val isSelected = viewModel.assignedDay == day
                            FilterChip(
                                selected = isSelected,
                                onClick = { 
                                    if (isSelected) viewModel.updateAssignedDay(null)
                                    else viewModel.updateAssignedDay(day)
                                },
                                label = { Text(day, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Lissu_Purple,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

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
                            onClick = {
                                viewModel.saveList()
                            },
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
                val focusRequester = remember { FocusRequester() }

                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Agregar producto") },
                    text = {
                        OutlinedTextField(
                            value = newItemName,
                            onValueChange = { newItemName = it },
                            label = { Text("Nombre del artículo") },
                            singleLine = true,
                            modifier = Modifier.focusRequester(focusRequester)
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

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
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
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
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
