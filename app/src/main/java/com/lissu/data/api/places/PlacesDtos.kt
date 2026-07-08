package com.lissu.data.api.places

import com.lissu.data.models.Place
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class PlacesResponseDto(
  val type: String,
  val features: List<PlacesFeatureDto>
)

@Serializable
data class PlacesFeatureDto(
  val type: String,
  val properties: PlaceDto,
  val geometry: JsonObject
)

@Serializable
data class PlaceDto(
  val name: String? = null,
  val country: String? = null,
  @SerialName("country_code") val countryCode: String? = null,
  val state: String? = null,
  val city: String? = null,
  val municipality: String? = null,
  val postcode: String? = null,
  val district: String? = null,
  val street: String? = null,
  @SerialName("iso3166_2") val iso31662: String? = null,
  val lon: Float,
  val lat: Float,
  @SerialName("state_code") val stateCode: String? = null,
  val formatted: String? = null,
  @SerialName("address_line1") val addressLine1: String? = null,
  @SerialName("address_line2") val addressLine2: String? = null,
  val categories: List<String>? = null,
  val details: List<String>? = null,
  val datasource: JsonObject? = null,
  val commercial: JsonObject? = null,
  @SerialName("place_id") val placeId: String
)

fun PlaceDto.toModel(): Place {
  return Place(
    name = name ?: "Supermercado",
    address = addressLine2 ?: "Dirección desconocida",
    lon = lon,
    lat = lat
  )
}