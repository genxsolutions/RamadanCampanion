package com.ramadan.companion.domain.preferences.repository

import com.ramadan.companion.domain.preferences.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Domain contract for user preferences (DataStore-backed).
 */
interface UserPreferencesRepository {

    val preferences: Flow<UserPreferences>

    suspend fun updatePreferences(block: (UserPreferences) -> UserPreferences)
}
