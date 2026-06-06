package com.lissu.data.repository

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

object KtorClient {

    private const val BASE_URL = "https://qjcxdvfzyseuvezacxsd.supabase.co/functions/v1/rankeuca/"

    private const val API_KEY = BuildConfig.API_TOKEN

    val client = HttpClient(OkHttp) {
        // Parseo automático de JSON
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        // Plugin de logging
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KtorClient", message)
                }
            }
            level = LogLevel.ALL
        }

        // Configuración aplicada a todas las peticiones
        defaultRequest {
            url(BASE_URL)
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.Authorization, "Bearer $API_KEY")
        }
    }
}