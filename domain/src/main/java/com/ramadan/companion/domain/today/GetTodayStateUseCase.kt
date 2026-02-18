package com.ramadan.companion.domain.today

import com.ramadan.companion.domain.today.model.TodayState
import com.ramadan.companion.domain.today.repository.TodayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case: observe Today screen state (single responsibility).
 * Business logic stays in domain; no Android dependencies.
 */
class GetTodayStateUseCase @Inject constructor(
    private val repository: TodayRepository
) {
    operator fun invoke(): Flow<TodayState> = repository.getTodayState()
}
