package com.ramadan.companion.domain.preferences.model

/**
 * User preferences for ibadah and reminders.
 */
data class UserPreferences(
    val preferredIbadah: List<IbadahType>,
    val maxSessionMinutes: Int,
    val remindersEnabled: Boolean
)

enum class IbadahType {
    QURAN,
    DHIKR,
    PRAYER,
    REFLECTION,
    DU_A
}
