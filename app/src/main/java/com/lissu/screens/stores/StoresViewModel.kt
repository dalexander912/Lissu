package com.lissu.screens.stores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lissu.data.models.Place
import com.lissu.data.repositories.PlacesRepository
import com.lissu.data.repositories.PlacesRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StoresViewModel : ViewModel() {
  private val repository: PlacesRepository = PlacesRepositoryImpl()

  private val _stores = MutableStateFlow<List<Place>>(emptyList())
  val stores = _stores.asStateFlow()

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

  private val _error = MutableStateFlow<String?>(null)
  val error: StateFlow<String?> = _error.asStateFlow()

  fun getStores(lon: Float, lat: Float) {
    viewModelScope.launch {
      _error.value = null
      _isLoading.value = true
      try {
        _stores.value = repository.getStores(lon, lat)
      } catch (e: Exception) {
        _error.value = "Error al buscar supermercados cercanos"
      }
      _isLoading.value = false
    }
  }

  fun refreshStores(lon: Float, lat: Float) {
    viewModelScope.launch {
      _error.value = null
      _isRefreshing.value = true
      try {
        _stores.value = repository.getStores(lon, lat)
      } catch (e: Exception) {
        _error.value = "Error al buscar supermercados cercanos"
      }
      _isRefreshing.value = false
    }
  }
}