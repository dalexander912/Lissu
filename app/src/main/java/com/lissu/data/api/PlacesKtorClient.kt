package com.lissu.data.api

import android.util.Log
import com.lissu.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object PlacesKtorClient {
  private const val BASE_URL = "https://api.geoapify.com"
  const val PLACES_KEY = BuildConfig.PLACES_TOKEN

  val client = HttpClient(OkHttp) {

    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }

    install(Logging) {
      logger = object : Logger {
        override fun log(message: String) {
          Log.d("AuthKtorClient", message)
        }
      }
      level = LogLevel.ALL
    }

    defaultRequest {
      url(BASE_URL)
      header(HttpHeaders.ContentType, "application/json")
      header(HttpHeaders.Accept, "application/json")
    }
  }
}