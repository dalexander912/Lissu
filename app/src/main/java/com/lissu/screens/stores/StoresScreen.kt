package com.lissu.screens.stores

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.lissu.AppScaffold
import com.lissu.R
import com.lissu.Routes
import com.lissu.components.StoreCard
import com.lissu.ui.theme.Lissu_DarkPurple
import com.lissu.ui.theme.Lissu_Purple2

@SuppressLint("MissingPermission")
private fun getLocation(
  context: Context,
  onLocationReceived: (Location?) -> Unit
) {
  val client = LocationServices.getFusedLocationProviderClient(context)

  client.getCurrentLocation(
    Priority.PRIORITY_HIGH_ACCURACY,
    CancellationTokenSource().token
  ).addOnSuccessListener {
    location -> onLocationReceived(location)
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun StoresScreen(
  viewModel: StoresViewModel = viewModel(),
  onBack: () -> Unit,
  onNavigateToHome: () -> Unit,
  onNavigateToAddList: () -> Unit,
  onNavigateToAccount: () -> Unit,
  onNavigateToReminders: () -> Unit
) {
  val isDark = isSystemInDarkTheme()

  val stores by viewModel.stores.collectAsState()
  val isLoading by viewModel.isLoading.collectAsState()
  val isRefreshing by viewModel.isRefreshing.collectAsState()
  val error by viewModel.error.collectAsState()

  val context = LocalContext.current
  val locationPermissionState = rememberPermissionState(
    Manifest.permission.ACCESS_FINE_LOCATION
  )

  var userLon by rememberSaveable { mutableStateOf<Float?>(null) }
  var userLat by rememberSaveable { mutableStateOf<Float?>(null) }

  LaunchedEffect(Unit) {
    if (!locationPermissionState.status.isGranted) {
      locationPermissionState.launchPermissionRequest()
    }
  }
  LaunchedEffect(locationPermissionState.status.isGranted) {
    if (locationPermissionState.status.isGranted) {
      getLocation(context) { location ->
        location?.let {
          viewModel.getStores(it.longitude.toFloat(), it.latitude.toFloat())
          userLon = it.longitude.toFloat()
          userLat = it.latitude.toFloat()
        }
      }
    }
  }

  if(userLon != null && userLat != null) {
    AppScaffold(
      title = "Supermercados",
      currentScreen = Routes.Maps,
      onBack = onBack,
      onNavigateToHome = onNavigateToHome,
      onNavigateToAddList = onNavigateToAddList,
      onNavigateToAccount = onNavigateToAccount,
      onNavigateToReminders = onNavigateToReminders
    ) { innerPadding ->
      PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshStores(userLon!!, userLat!!) },
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        if(isLoading) {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
          }
        }
        if(error != null) {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.ErrorOutline,
              contentDescription = null,
              modifier = Modifier.size(64.dp)
            )
            Text("Error al buscar supermercados cercanos")
            Spacer(Modifier.height(16.dp))
            Button({ viewModel.getStores(userLon!!, userLat!!) }) {
              Text("Reintentar")
            }
          }
        }

        if(!isLoading && error == null) {
          Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
          ) {
            Column(modifier = Modifier
              .fillMaxWidth()
              .background(if(isDark) Lissu_DarkPurple else Lissu_Purple2)
              .padding(top = 28.dp),
              horizontalAlignment = Alignment.End
            ) {
              Icon(
                painter = painterResource(R.drawable.maps_search),
                contentDescription = null,
                tint = if(isDark) Lissu_Purple2 else Color.Black,
                modifier = Modifier.size(96.dp).alpha(0.5f)
              )
            }

            Spacer(Modifier.height(8.dp))

            Column(modifier = Modifier.padding(12.dp)) {
              Text(
                text = "Resultados cerca de tí:",
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
              )
              Spacer(Modifier.height(8.dp))
              for (store in stores) {
                StoreCard(store, userLon!!, userLat!!)
                Spacer(Modifier.height(12.dp))
              }
            }
          }
        }
      }
    }
  } else {
    AppScaffold(
      title = "Supermercados",
      currentScreen = Routes.Maps,
      onBack = onBack,
      onNavigateToHome = onNavigateToHome,
      onNavigateToAddList = onNavigateToAddList,
      onNavigateToAccount = onNavigateToAccount,
      onNavigateToReminders = onNavigateToReminders
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Default.ErrorOutline,
          contentDescription = null,
          modifier = Modifier.size(64.dp)
        )
        Text("No se pudo obtener tu ubicación")
      }
    }
  }
}