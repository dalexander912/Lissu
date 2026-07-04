package com.lissu.data.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
  val name: String,
  val email: String,
  val password: String
)

@Serializable
data class LoginRequestDto(
  val email: String,
  val password: String
)

@Serializable
data class LoginResponseDto(
  val token: String,
  val user: UserDto
)

@Serializable
data class UserDto(
  val id: Int,
  val name: String,
  val email: String
)