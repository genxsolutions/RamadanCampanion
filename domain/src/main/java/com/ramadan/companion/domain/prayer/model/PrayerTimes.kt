package com.ramadan.companion.domain.prayer.model

/**
 * Immutable domain model for daily prayer times.
 * All times in minutes since midnight (0–1439) for timezone-agnostic calculation.
 */
data class PrayerTimes(
    val date: String, // yyyy-MM-dd
    val fajr: Int,
    val dhuhr: Int,
    val asr: Int,
    val maghrib: Int,
    val isha: Int
)
