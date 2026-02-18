package com.ramadan.companion.core.database.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.ramadan.companion.domain.preferences.model.IbadahType
import com.ramadan.companion.domain.preferences.model.UserPreferences
import com.ramadan.companion.domain.preferences.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override val preferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        UserPreferences(
            preferredIbadah = (prefs[KEY_PREFERRED_IBADAH] ?: DEFAULT_IBADAH).mapNotNull { it.toIntOrNull()?.let { ord -> IbadahType.entries.getOrNull(ord) } },
            maxSessionMinutes = prefs[KEY_MAX_SESSION_MINUTES] ?: DEFAULT_MAX_SESSION,
            remindersEnabled = prefs[KEY_REMINDERS_ENABLED] ?: true
        )
    }

    override suspend fun updatePreferences(block: (UserPreferences) -> UserPreferences) {
        dataStore.edit { prefs ->
            val current = UserPreferences(
                preferredIbadah = (prefs[KEY_PREFERRED_IBADAH] ?: DEFAULT_IBADAH).mapNotNull { it.toIntOrNull()?.let { ord -> IbadahType.entries.getOrNull(ord) } },
                maxSessionMinutes = prefs[KEY_MAX_SESSION_MINUTES] ?: DEFAULT_MAX_SESSION,
                remindersEnabled = prefs[KEY_REMINDERS_ENABLED] ?: true
            )
            val updated = block(current)
            prefs[KEY_PREFERRED_IBADAH] = updated.preferredIbadah.map { it.ordinal.toString() }.toSet()
            prefs[KEY_MAX_SESSION_MINUTES] = updated.maxSessionMinutes
            prefs[KEY_REMINDERS_ENABLED] = updated.remindersEnabled
        }
    }

    companion object {
        private val KEY_PREFERRED_IBADAH = stringSetPreferencesKey("preferred_ibadah")
        private val KEY_MAX_SESSION_MINUTES = intPreferencesKey("max_session_minutes")
        private val KEY_REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        private val DEFAULT_IBADAH = setOf("0", "1") // QURAN, DHIKR
        private const val DEFAULT_MAX_SESSION = 15
    }
}
