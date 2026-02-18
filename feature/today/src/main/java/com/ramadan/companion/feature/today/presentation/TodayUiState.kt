package com.ramadan.companion.feature.today.presentation

import com.ramadan.companion.domain.today.model.QuickAction

/**
 * Immutable UI state for Today screen.
 * Single source of truth for the UI; mapped from domain [TodayState].
 *
 * Kept intentionally simple and string-based so the UI does not depend
 * directly on domain formatting details and is easy to preview.
 */
data class TodayUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userName: String = "",
    val ramadanDay: Int = 0,
    val progressPercent: Float = 0f,
    val nextPrayer: String = "",
    val suggestedIbadah: String = "",
    val quickActions: List<QuickAction> = emptyList()
)
