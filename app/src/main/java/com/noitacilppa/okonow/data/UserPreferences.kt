package com.noitacilppa.okonow.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_IMAGE_URI_KEY = stringPreferencesKey("user_image_uri")
    }

    val userName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY]
        }

    val userImageUri: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_IMAGE_URI_KEY]
        }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    suspend fun saveUserImageUri(uri: String?) {
        context.dataStore.edit { preferences ->
            if (uri != null) {
                preferences[USER_IMAGE_URI_KEY] = uri
            } else {
                preferences.remove(USER_IMAGE_URI_KEY)
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
