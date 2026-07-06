package com.lissu.data.repositories

import com.lissu.data.models.Place

interface PlacesRepository {
  suspend fun getStores(lon: Float, lat: Float): List<Place>
}