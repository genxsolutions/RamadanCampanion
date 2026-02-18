package com.ramadan.companion.domain.prayer.repository

import com.ramadan.companion.domain.prayer.model.PrayerTimes
import kotlinx.coroutines.flow.Flow

/**
 * Domain contract for prayer times.
 * Implementations fetch from API using lat/lon/date and cache per day.
 */
interface PrayerTimeRepository {

    /**
     * Stream of cached prayer times for the current day (refreshed when location/date changes).
     */
    fun getPrayerTimesFlow(): Flow<PrayerTimes?>

    /**
     * Fetch prayer times for the given parameters and update cache.
     */
    suspend fun getPrayerTimes(
        lat: Double,
        lon: Double,
        date: String
    ): PrayerTimes

    suspend fun refreshPrayerTimes()
}
