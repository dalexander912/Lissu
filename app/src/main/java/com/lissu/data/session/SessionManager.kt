package com.lissu.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

  companion object {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val NAME_KEY = stringPreferencesKey("user_name")
  }

  // Lectura reactiva: emite cada vez que cambia
  val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
  val userName: Flow<String?> = context.dataStore.data.map { it[NAME_KEY] }

  suspend fun save(token: String, name: String) {
    context.dataStore.edit { prefs ->
      prefs[TOKEN_KEY] = token
      prefs[NAME_KEY] = name
    }
  }

  suspend fun clear() {
    context.dataStore.edit { it.clear() }
  }
}