package com.ramadan.companion.feature.companion.data

import com.ramadan.companion.domain.companion.AiCompanionRepository
import com.ramadan.companion.domain.companion.DailyContext
import javax.inject.Inject

/**
 * Fake AI repository used during early development.
 * Returns gentle, wellness-style suggestions without hitting a real backend.
 */
class FakeAiCompanionRepository @Inject constructor() : AiCompanionRepository {

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
}

