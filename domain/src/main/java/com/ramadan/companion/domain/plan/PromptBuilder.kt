package com.ramadan.companion.domain.plan

import com.ramadan.companion.domain.plan.model.UserContext

/**
 * Domain-only prompt construction for AI planner.
 * No API keys or network; used by data layer to build the request body.
 */
object PromptBuilder {

    fun buildTodayPlanPrompt(context: UserContext): String = buildString {
        appendLine("Generate a short, calm Ramadan daily plan.")
        appendLine("User has ${context.availableMinutes} minutes available.")
        appendLine("Energy level: ${context.energyLevel.name.lowercase()}.")
        appendLine("Next prayer: ${context.nextPrayer}.")
        appendLine("Ramadan day: ${context.ramadanDay}.")
        appendLine("Preferred worship: ${context.preferences.preferredIbadah.joinToString { it.name.lowercase() }}.")
        appendLine("Max session length: ${context.preferences.maxSessionMinutes} minutes.")
        appendLine("Return exactly: 1) A short title (e.g. 'A calm moment before Maghrib'), 2) A one-line subtitle, 3) A list of 1-2 suggested actions (e.g. 'Read 2 pages Quran', '5 min dhikr').")
    }
}
