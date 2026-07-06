package com.lissu.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lissu.R
import com.lissu.data.models.Place
import kotlin.math.cos
import kotlin.math.sqrt

@Composable
fun StoreCard(
  store: Place,
  userLon: Float,
  userLat: Float
) {
  val isDark = isSystemInDarkTheme()

  val distance = getDistance(userLon, userLat, store.lon, store.lat)
  val roundedDistance = String.format("%.2f", distance)

  Card(
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = store.name,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.weight(1f)
        )
        Icon(
          painter = painterResource(R.drawable.store),
          contentDescription = null,
          tint = if(isDark) Color.White else Color.Black,
          modifier = Modifier.size(32.dp)
        )
      }

      HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(8.dp))

      Text(
        text = store.address,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )
      Spacer(Modifier.height(8.dp))

      Row(modifier = Modifier.fillMaxWidth()) {
        Icon(
          painter = painterResource(R.drawable.distance),
          contentDescription = null,
          tint = if(isDark) Color.White else Color.Black,
          modifier = Modifier.size(20.dp)
        )
        Text(
          text = "Distancia: $roundedDistance km",
          fontSize = 12.sp
        )
      }
    }
  }
}

// Distancia muy aproximada entre dos coordenadas geográficas
fun getDistance(
  lon1: Float, lat1: Float,
  lon2: Float, lat2: Float
): Float {
  val deltaLon = lon2 - lon1
  val deltaLat = lat2 - lat1
  val avrgLat = (lat1 + lat2) / 2

  // Longitud: 1° aprox.= 111.32 * cos(latitud_media) km
  // Latitud: 1° aprox.= 111.32 km
  val dx = 111.32 * cos(avrgLat) * deltaLon
  val dy = 111.32 * deltaLat

  val distance = sqrt(dx*dx + dy*dy)

  return distance.toFloat()
}