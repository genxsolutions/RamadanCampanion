package com.ramadan.companion.feature.today.data.prayer

import com.ramadan.companion.core.network.prayer.AladhanApi
import com.ramadan.companion.domain.location.LocationProvider
import com.ramadan.companion.domain.prayer.model.PrayerTimes
import com.ramadan.companion.domain.prayer.repository.PrayerTimeRepository
import com.ramadan.companion.domain.time.Clock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Default location (Mecca) when device location is unavailable (e.g. permission denied). */
private const val DEFAULT_LAT = 21.4225
private const val DEFAULT_LON = 39.8262

/**
 * Fetches prayer times from Aladhan API using current location.
 * Falls back to default location when location is unavailable so prayer times and progress still show.
 * Caches result for the day; refresh on demand or when date changes.
 */
class PrayerTimeRepositoryImpl @Inject constructor(
    private val api: AladhanApi,
    private val locationProvider: LocationProvider,
    private val clock: Clock
) : PrayerTimeRepository {

    private val cache = MutableStateFlow<PrayerTimes?>(null)
    private var cachedDate: String? = null

    override fun getPrayerTimesFlow(): StateFlow<PrayerTimes?> = cache.asStateFlow()

    override suspend fun getPrayerTimes(
        lat: Double,
        lon: Double,
        date: String
    ): PrayerTimes {
        val dateForApi = date.split("-").let { parts ->
            if (parts.size == 3) "${parts[2]}-${parts[1]}-${parts[0]}" else date
        }
         val response = api.getTimings(latitude = lat, longitude = lon, date = dateForApi)
        val times = mapToDomain(response, date)
        if (date == clock.todayDateKey()) {
            cache.value = times
            cachedDate = date
        }
        return times
    }

    override suspend fun refreshPrayerTimes() {
        val date = clock.todayDateKey()
        if (cachedDate == date && cache.value != null) return
        var lat: Double
        var lon: Double
        try {
            val loc = locationProvider.getCurrentLocation()
            lat = loc.latitude
            lon = loc.longitude
        } catch (_: Exception) {
            lat = DEFAULT_LAT
            lon = DEFAULT_LON
        }
        try {
            getPrayerTimes(lat, lon, date)
        } catch (_: Exception) {
            // Keep last cache on failure (offline resilience)
        }
    }

    private fun mapToDomain(response: com.ramadan.companion.core.network.prayer.AladhanResponse, date: String): PrayerTimes {
        val t = response.data.timings
        return PrayerTimes(
            date = date,
            fajr = parseMinutes(t.Fajr),
            dhuhr = parseMinutes(t.Dhuhr),
            asr = parseMinutes(t.Asr),
            maghrib = parseMinutes(t.Maghrib),
            isha = parseMinutes(t.Isha)
        )
    }

    private fun parseMinutes(timeStr: String): Int {
        val trimmed = timeStr.trim()
        val timePart = trimmed.split(" ").firstOrNull() ?: trimmed
        val (h, m) = timePart.split(":").let { list ->
            val hour = list.getOrNull(0)?.toIntOrNull() ?: 0
            val min = list.getOrNull(1)?.toIntOrNull() ?: 0
            hour to min
        }
        return (h % 24) * 60 + m
    }
}
