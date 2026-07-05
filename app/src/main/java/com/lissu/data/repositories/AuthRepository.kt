package com.lissu.data.repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  val isLoggedIn: Flow<Boolean>
  val userName: Flow<String?>
  val email: Flow<String?>

  suspend fun register(name: String, email: String, password: String)
  suspend fun login(email: String, password: String)
  suspend fun logout()
}