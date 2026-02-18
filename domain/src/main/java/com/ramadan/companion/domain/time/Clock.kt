package com.ramadan.companion.domain.time

/**
 * Abstraction for current time (minutes since midnight, date key).
 * Implemented in data layer for testability.
 */
interface Clock {

    fun currentMinutesOfDay(): Int
    fun todayDateKey(): String // yyyy-MM-dd
}
