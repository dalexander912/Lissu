package com.lissu.data.repositories

import com.lissu.data.api.AuthKtorClient
import com.lissu.data.api.auth.LoginRequestDto
import com.lissu.data.api.auth.LoginResponseDto
import com.lissu.data.api.auth.RegisterRequestDto
import com.lissu.data.session.SessionManager
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
  private val session: SessionManager
) : AuthRepository {

  // Hay sesión si hay token guardado
  override val isLoggedIn: Flow<Boolean> = session.token.map { it != null }
  override val userName: Flow<String?> = session.userName
  override val email: Flow<String?> = session.email

  override suspend fun register(name: String, email: String, password: String) {
    val response: LoginResponseDto = AuthKtorClient.client.post("auth/register") {
      contentType(ContentType.Application.Json)
      setBody(RegisterRequestDto(name, email, password))
    }.body()

    // Login exitoso: guardamos la sesión
    session.save(response.token, response.user.name, email)
  }

  override suspend fun login(email: String, password: String) {
    val response: LoginResponseDto = AuthKtorClient.client.post("auth/login") {
      contentType(ContentType.Application.Json)
      setBody(LoginRequestDto(email, password))
    }.body()

    // Login exitoso: guardamos la sesión
    session.save(response.token, response.user.name, email)
  }

  override suspend fun logout() {
    session.clear()
  }
}