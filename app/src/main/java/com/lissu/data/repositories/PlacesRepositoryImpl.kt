package com.lissu.data.repositories

import com.lissu.data.api.PlacesKtorClient
import com.lissu.data.api.places.PlacesResponseDto
import com.lissu.data.api.places.toModel
import com.lissu.data.models.Place
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PlacesRepositoryImpl: PlacesRepository {

  override suspend fun getStores(lon: Float, lat: Float): List<Place> {
    val response: PlacesResponseDto = PlacesKtorClient.client.get("/v2/places") {
      parameter("categories", "commercial.supermarket")
      parameter("bias", "proximity:$lon,$lat")
      parameter("limit", "15") //Obtener los 15 primeros resultados
      parameter("apiKey", PlacesKtorClient.PLACES_KEY)
    }.body()

    return response.features.map { it.properties.toModel() }
  }
}