package com.lissu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lissu.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
  private val repository: AuthRepository
) : ViewModel() {

  // null = todavía no sabemos (DataStore cargando)
  val isLoggedIn: StateFlow<Boolean?> = repository.isLoggedIn
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  val userName: StateFlow<String?> = repository.userName
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  private val _error = MutableStateFlow<String?>(null)
  val error: StateFlow<String?> = _error.asStateFlow()

  fun register(name: String, email: String, password: String) {
    viewModelScope.launch {
      _error.value = null
      _isLoading.value = true
      try {
        repository.register(name, email, password)
      } catch (e: Exception) {
        _error.value = "Error al intentar crear el usuario"
      }
      _isLoading.value = false
    }
  }

  fun login(email: String, password: String) {
    viewModelScope.launch {
      _error.value = null
      _isLoading.value = true
      try {
        repository.login(email, password)
      } catch (e: Exception) {
        _error.value = "Correo o contraseña incorrectos"
      }
      _isLoading.value = false
    }
  }

  fun logout() {
    viewModelScope.launch { repository.logout() }
  }

  companion object {
    val Factory = viewModelFactory {
      initializer {
        val app =
          this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as LissuApplication
        AuthViewModel(app.appProvider.provideAuthRepository())
      }
    }
  }
}