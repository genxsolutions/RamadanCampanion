package com.ramadan.companion.domain.today.repository

import com.ramadan.companion.domain.today.model.TodayState
import kotlinx.coroutines.flow.Flow

/**
 * Domain contract for Today screen data.
 * Implemented in data layer; used by use cases.
 */
interface TodayRepository {
    fun getTodayState(): Flow<TodayState>
    suspend fun refreshTodayState()
}
