package com.ramadan.companion.domain.prayer.model

/**
 * Result of resolving the next upcoming prayer.
 */
data class NextPrayerInfo(
    val name: String,
    val timeFormatted: String,
    val countdownText: String
)
