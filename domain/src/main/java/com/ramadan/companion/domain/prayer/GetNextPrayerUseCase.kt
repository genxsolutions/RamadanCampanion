package com.ramadan.companion.domain.prayer

import com.ramadan.companion.domain.prayer.model.NextPrayerInfo
import com.ramadan.companion.domain.prayer.model.PrayerTimes
import javax.inject.Inject

/**
 * Pure business logic: given prayer times and current time (minutes since midnight),
 * returns the next upcoming prayer with formatted time and countdown.
 * Handles day rollover (after Isha -> next is Fajr next day).
 */
class GetNextPrayerUseCase @Inject constructor() {

    fun getNextPrayer(
        prayerTimes: PrayerTimes,
        currentMinutesOfDay: Int
    ): NextPrayerInfo {
        val order = listOf(
            Prayer.FAJR to prayerTimes.fajr,
            Prayer.DHUHR to prayerTimes.dhuhr,
            Prayer.ASR to prayerTimes.asr,
            Prayer.MAGHRIB to prayerTimes.maghrib,
            Prayer.ISHA to prayerTimes.isha
        )
        val minutesInDay = 24 * 60
        for ((name, minutes) in order) {
            if (currentMinutesOfDay < minutes) {
                val remaining = minutes - currentMinutesOfDay
                return NextPrayerInfo(
                    name = name.displayName,
                    timeFormatted = formatMinutes(minutes),
                    countdownText = formatCountdown(remaining)
                )
            }
        }
        val fajrNextDay = minutesInDay + order[0].second
        val remaining = fajrNextDay - currentMinutesOfDay
        return NextPrayerInfo(
            name = "${Prayer.FAJR.displayName} (tomorrow)",
            timeFormatted = formatMinutes(order[0].second),
            countdownText = formatCountdown(remaining)
        )
    }

    private fun formatMinutes(minutes: Int): String {
        val h = minutes / 60
        val m = minutes % 60
        val amPm = if (h >= 12) "PM" else "AM"
        val hour12 = if (h == 0) 12 else if (h > 12) h - 12 else h
        return String.format("%d:%02d %s", hour12, m, amPm)
    }

    private fun formatCountdown(totalMinutes: Int): String {
        if (totalMinutes <= 0) return "now"
        val hours = totalMinutes / 60
        val mins = totalMinutes % 60
        return when {
            hours > 0 -> "in $hours ${if (hours == 1) "hour" else "hours"} $mins min"
            else -> "in $mins min"
        }
    }

    private enum class Prayer(val displayName: String) {
        FAJR("Fajr"),
        DHUHR("Dhuhr"),
        ASR("Asr"),
        MAGHRIB("Maghrib"),
        ISHA("Isha")
    }
}
