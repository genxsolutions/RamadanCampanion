package com.ramadan.companion.feature.today.data.plan

import com.ramadan.companion.core.ai.AIService
import com.ramadan.companion.core.ai.AiApiKey
import com.ramadan.companion.core.ai.AiMessage
import com.ramadan.companion.core.ai.AiRequest
import com.ramadan.companion.core.ai.ResponseParser
import com.ramadan.companion.domain.plan.PromptBuilder
import com.ramadan.companion.domain.plan.model.TodayPlan
import com.ramadan.companion.domain.plan.model.UserContext
import com.ramadan.companion.domain.plan.repository.AiPlannerRepository
import javax.inject.Inject

/**
 * Calls AI service when API key is present; otherwise uses fallback heuristic.
 * Caching of last plan is done by the caller (ViewModel).
 */
class AiPlannerRepositoryImpl @Inject constructor(
    private val api: AIService,
    @AiApiKey private val apiKey: String
) : AiPlannerRepository {

    override suspend fun generateTodayPlan(context: UserContext): TodayPlan {
        return if (apiKey.isNotBlank()) {
            try {
                val prompt = PromptBuilder.buildTodayPlanPrompt(context)
                val request = AiRequest(
                    messages = listOf(
                        AiMessage(role = "user", content = prompt)
                    )
                )
                val response = api.getCompletion(
                    authorization = "Bearer $apiKey",
                    body = request
                )
                val content = response.choices?.firstOrNull()?.message?.content ?: ""
                ResponseParser.parse(content)
            } catch (_: Exception) {
                buildFallbackPlan(context)
            }
        } else {
            buildFallbackPlan(context)
        }
    }

    private fun buildFallbackPlan(context: UserContext): TodayPlan {
        val actions = when (context.energyLevel) {
            com.ramadan.companion.domain.plan.model.EnergyLevel.LOW -> listOf("5 min dhikr", "Short reflection")
            com.ramadan.companion.domain.plan.model.EnergyLevel.MEDIUM -> listOf("Read 2 pages Quran", "5 min dhikr")
            com.ramadan.companion.domain.plan.model.EnergyLevel.HIGH -> listOf("Read 3 pages Quran", "10 min dhikr", "Gratitude note")
        }
        val title = "A calm moment before ${context.nextPrayer}"
        val subtitle = "You have ${context.availableMinutes} minutes. Here's a gentle plan."
        return TodayPlan(title = title, subtitle = subtitle, suggestedActions = actions.take(2))
    }
}
