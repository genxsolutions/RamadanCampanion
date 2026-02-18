package com.ramadan.companion.domain.companion

/**
 * Domain contract for AI Companion suggestions.
 * Implementation lives in the data layer and can be backed by a real AI API later.
 */
interface AiCompanionRepository {
    suspend fun getDailySuggestion(context: DailyContext): String
}

/**
 * Minimal context for generating daily AI guidance.
 */
data class DailyContext(
    val ramadanDay: Int,
    val recentEnergyLevel: Int,
    val lastPrayerCompleted: String?,
    val hasReflectionToday: Boolean
)

