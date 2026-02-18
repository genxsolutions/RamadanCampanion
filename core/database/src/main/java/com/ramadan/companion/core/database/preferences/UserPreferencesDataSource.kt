package com.ramadan.companion.core.database.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val userName: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_USER_NAME] ?: DEFAULT_USER_NAME
    }

    suspend fun setUserName(name: String) {
        dataStore.edit { it[KEY_USER_NAME] = name }
    }

    companion object {
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private const val DEFAULT_USER_NAME = "Guest"
    }
}
