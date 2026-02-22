package com.ramadan.companion.feature.companion.data

import com.ramadan.companion.core.ai.AIService
import com.ramadan.companion.core.ai.AiApiKey
import com.ramadan.companion.core.ai.AiMessage
import com.ramadan.companion.core.ai.AiRequest
import com.ramadan.companion.domain.companion.AiCompanionRepository
import com.ramadan.companion.domain.companion.DailyContext
import javax.inject.Inject

/**
 * AI Companion repository: uses real API when [apiKey] is set, otherwise fallback text.
 */
class FakeAiCompanionRepository @Inject constructor(
    private val api: AIService,
    @AiApiKey private val apiKey: String
) : AiCompanionRepository {

    override suspend fun getDailySuggestion(context: DailyContext): String {
        val focus = when {
            context.recentEnergyLevel <= 3 -> "a very light, nurturing routine"
            context.recentEnergyLevel <= 6 -> "a balanced mix of Quran, dhikr, and rest"
            else -> "a focused burst of Quran and reflection"
        }

        val reflectionHint = if (context.hasReflectionToday) {
            "Since you already reflected today, you can keep this moment very gentle."
        } else {
            "Consider ending this session with a short gratitude reflection."
        }

        val lastPrayer = context.lastPrayerCompleted ?: "your last prayer"

        return buildString {
            appendLine("Let's design a calm plan around $lastPrayer.")
            appendLine()
            appendLine("For today, I recommend $focus:")
            appendLine("• 5–10 minutes of Quran recitation at your own pace")
            appendLine("• A slow dhikr session focusing on 'Alhamdulillah'")
            appendLine("• A deep breath before and after each segment")
            appendLine()
            append(reflectionHint)
        }.trim()
    }

    override suspend fun sendMessage(userMessage: String, history: List<Pair<String, String>>): String {
        if (apiKey.isBlank()) {
            return fallbackReply()
        }
        return try {
            val messages = mutableListOf<AiMessage>()
            messages.add(
                AiMessage(
                    role = "system",
                    content = "You are a gentle, supportive spiritual wellness companion for Ramadan. " +
                        "Keep replies concise, warm, and uplifting. Use 1-3 short paragraphs."
                )
            )
            history.forEach { (user, assistant) ->
                messages.add(AiMessage(role = "user", content = user))
                messages.add(AiMessage(role = "assistant", content = assistant))
            }
            messages.add(AiMessage(role = "user", content = userMessage))
            val request = AiRequest(
                messages = messages,
                max_tokens = 320
            )
            val response = api.getCompletion(
                authorization = "Bearer $apiKey",
                body = request
            )
            response.choices?.firstOrNull()?.message?.content?.trim() ?: fallbackReply()
        } catch (_: Exception) {
            fallbackReply()
        }
    }

    private fun fallbackReply(): String =
        "I'm here for you. Take a moment to breathe, and whenever you're ready, share a little more."
}

