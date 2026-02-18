package com.ramadan.companion.feature.today.data.repository

import com.ramadan.companion.core.database.preferences.UserPreferencesDataSource
import com.ramadan.companion.domain.today.model.DayTimeline
import com.ramadan.companion.domain.today.model.PrayerInfo
import com.ramadan.companion.domain.today.model.QuickAction
import com.ramadan.companion.domain.today.model.SuggestedIbadah
import com.ramadan.companion.domain.today.model.TodayState
import com.ramadan.companion.domain.today.repository.TodayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Offline-first implementation of TodayRepository.
 * Uses DataStore for user prefs; state is derived and cached.
 */
class TodayRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferencesDataSource
) : TodayRepository {

    override fun getTodayState(): Flow<TodayState> = combine(
        userPreferences.userName,
        flowOf(DUMMY_RAMADAN_DAY),
        flowOf(DUMMY_PROGRESS),
        flowOf(DUMMY_NEXT_PRAYER),
        flowOf(DUMMY_SUGGESTED_IBADAH),
        flowOf(DUMMY_TIMELINE),
        flowOf(DUMMY_QUICK_ACTIONS)
    ) { args ->
        TodayState(
            userName = args[0] as String,
            ramadanDay = args[1] as Int,
            progressPercent = args[2] as Float,
            nextPrayer = args[3] as PrayerInfo,
            suggestedIbadah = args[4] as SuggestedIbadah,
            dayTimeline = args[5] as DayTimeline,
            quickActions = args[6] as List<QuickAction>
        )
    }

    override suspend fun refreshTodayState() {
        // In production: recalc from prayer times API, local cache invalidation, etc.
    }

    companion object {
        private const val DUMMY_RAMADAN_DAY = 14
        private const val DUMMY_PROGRESS = 0.65f
        private val DUMMY_NEXT_PRAYER = PrayerInfo(
            name = "Maghrib",
            timeFormatted = "6:42 PM",
            countdownText = "in 2 hours 45 minutes"
        )
        private val DUMMY_SUGGESTED_IBADAH = SuggestedIbadah(
            contextMessage = "You have 15 calm minutes before work",
            items = listOf("Read 2 pages Quran", "5 min dhikr")
        )
        private val DUMMY_TIMELINE = DayTimeline(
            suhoorTime = "4:30 AM",
            suhoorDone = true,
            currentTime = "3:15 PM",
            iftarTime = "6:42 PM",
            iftarDone = false
        )
        private val DUMMY_QUICK_ACTIONS = listOf(
            QuickAction("dhikr", "Quick Dhikr", false),
            QuickAction("energy", "Energy Check", false),
            QuickAction("ai", "Ask AI", true)
        )
    }
}
