package com.ramadan.companion.domain.time

/**
 * Provides the current day of Ramadan (1–30) from the Islamic (Hijri) calendar.
 * Returns 0 when today is not in the month of Ramadan.
 */
interface RamadanDayProvider {

    fun getRamadanDay(): Int
}
