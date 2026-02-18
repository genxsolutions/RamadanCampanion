package com.ramadan.companion.domain.plan.model

import com.ramadan.companion.domain.preferences.model.UserPreferences

/**
 * Context passed to AI planner for generating today's plan.
 */
data class UserContext(
    val availableMinutes: Int,
    val energyLevel: EnergyLevel,
    val preferences: UserPreferences,
    val currentTimeMinutes: Int,
    val nextPrayer: String,
    val ramadanDay: Int
)

enum class EnergyLevel {
    LOW,
    MEDIUM,
    HIGH
}
