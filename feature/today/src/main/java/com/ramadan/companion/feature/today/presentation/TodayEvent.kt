package com.ramadan.companion.feature.today.presentation

/**
 * Sealed UI events for Today screen.
 * All user actions flow through these events; ViewModel handles side effects.
 */
sealed interface TodayEvent {
    data object Refresh : TodayEvent
    data class QuickActionClicked(val actionId: String) : TodayEvent
}
