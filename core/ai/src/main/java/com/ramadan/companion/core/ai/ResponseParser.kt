package com.ramadan.companion.core.ai

import com.ramadan.companion.domain.plan.model.TodayPlan

/**
 * Parses AI response (or fallback text) into domain TodayPlan.
 */
object ResponseParser {

    fun parse(aiContent: String): TodayPlan {
        val lines = aiContent.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val title = lines.firstOrNull()?.takeIf { it.length < 80 } ?: "Your calm moment"
        val subtitle = lines.getOrNull(1)?.takeIf { it.length < 120 } ?: "A gentle plan for today"
        val actions = lines.drop(2).filter { it.length in 1..100 }.take(3)
        return TodayPlan(
            title = title,
            subtitle = subtitle,
            suggestedActions = if (actions.isEmpty()) listOf("5 min dhikr", "Read a page of Quran") else actions
        )
    }
}
