package com.ramadan.companion.domain.companion

/**
 * Domain contract for AI Companion suggestions and chat.
 * Implementations can use a real AI API (e.g. OpenAI-style) for chat.
 */
interface AiCompanionRepository {
    suspend fun getDailySuggestion(context: DailyContext): String

    /**
     * Send a user message and get an AI reply. [history] is (user, assistant) pairs in order.
     * Returns the assistant's reply text.
     */
    suspend fun sendMessage(userMessage: String, history: List<Pair<String, String>>): String
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

