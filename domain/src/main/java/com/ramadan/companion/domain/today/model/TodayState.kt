package com.ramadan.companion.domain.today.model

/**
 * Immutable domain model for "Today" screen data.
 * Single source of truth for business logic.
 */
data class TodayState(
    val userName: String,
    val ramadanDay: Int,
    val progressPercent: Float,
    val nextPrayer: PrayerInfo,
    val suggestedIbadah: SuggestedIbadah?,
    val dayTimeline: DayTimeline,
    val quickActions: List<QuickAction>
)

data class PrayerInfo(
    val name: String,
    val timeFormatted: String,
    val countdownText: String
)

data class SuggestedIbadah(
    val contextMessage: String,
    val items: List<String>
)

data class DayTimeline(
    val suhoorTime: String,
    val suhoorDone: Boolean,
    val currentTime: String,
    val iftarTime: String,
    val iftarDone: Boolean
)

data class QuickAction(
    val id: String,
    val label: String,
    val isPrimary: Boolean
)
