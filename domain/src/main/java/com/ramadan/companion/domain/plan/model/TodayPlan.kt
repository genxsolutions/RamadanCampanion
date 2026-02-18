package com.ramadan.companion.domain.plan.model

/**
 * AI-generated or fallback plan for the Today screen flow cards.
 */
data class TodayPlan(
    val title: String,
    val subtitle: String,
    val suggestedActions: List<String>
)
