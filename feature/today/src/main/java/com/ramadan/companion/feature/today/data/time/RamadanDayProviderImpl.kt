package com.ramadan.companion.feature.today.data.time

import android.icu.util.Calendar
import android.icu.util.IslamicCalendar
import com.ramadan.companion.domain.time.RamadanDayProvider
import javax.inject.Inject

/**
 * Returns the current Ramadan day (1–30) using the Islamic (Hijri) calendar.
 * Uses [IslamicCalendar] (Umm al-Qura / civil) so the day matches the actual Islamic date.
 */
class RamadanDayProviderImpl @Inject constructor() : RamadanDayProvider {

    override fun getRamadanDay(): Int {
        val cal = IslamicCalendar()
        cal.timeInMillis = System.currentTimeMillis()
        // IslamicCalendar.MONTH is 0-based; Ramadan is the 9th month → 8
        if (cal.get(Calendar.MONTH) != IslamicCalendar.RAMADAN) return 0
        return cal.get(Calendar.DAY_OF_MONTH).coerceIn(1, 30)
    }
}
