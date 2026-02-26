package com.ramadan.companion.feature.today.presentation

import androidx.lifecycle.viewModelScope
import com.ramadan.companion.core.database.preferences.UserPreferencesDataSource
import com.ramadan.companion.domain.plan.GenerateTodayPlanUseCase
import com.ramadan.companion.domain.plan.model.EnergyLevel
import com.ramadan.companion.domain.plan.model.TodayPlan
import com.ramadan.companion.domain.plan.model.UserContext
import com.ramadan.companion.domain.prayer.GetNextPrayerUseCase
import com.ramadan.companion.domain.prayer.model.NextPrayerInfo
import com.ramadan.companion.domain.prayer.repository.PrayerTimeRepository
import com.ramadan.companion.domain.preferences.repository.UserPreferencesRepository
import com.ramadan.companion.domain.time.Clock
import com.ramadan.companion.domain.time.RamadanDayProvider
import com.ramadan.companion.domain.today.model.QuickAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Combines prayer times, AI plan, and preferences into a single UI state.
 * No business logic here; only use cases and repositories.
 */
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val getNextPrayerUseCase: GetNextPrayerUseCase,
    private val clock: Clock,
    private val ramadanDayProvider: RamadanDayProvider,
    private val generateTodayPlanUseCase: GenerateTodayPlanUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : androidx.lifecycle.ViewModel() {

    private val _state = MutableStateFlow(TodayUiState(isLoading = true))
    val state: StateFlow<TodayUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<TodaySideEffect>(replay = 0)
    val events = _events

    private val cachedPlan = MutableStateFlow<TodayPlan?>(null)
    private var planJob: Job? = null

    init {
        viewModelScope.launch {
            try {
                prayerTimeRepository.refreshPrayerTimes()
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = "Location or prayer times unavailable") }
            }
        }
        observeCombinedState()
    }

    fun handleEvent(event: TodayEvent) {
        when (event) {
            is TodayEvent.Refresh -> refresh()
            is TodayEvent.QuickActionClicked -> onQuickActionClicked(event.actionId)
        }
    }

    private fun observeCombinedState() {
        val timeTickFlow = flow {
            emit(Unit)
            while (true) {
                delay(60_000)
                emit(Unit)
            }
        }
        val prayerTimesFlow = prayerTimeRepository.getPrayerTimesFlow()
        val nextPrayerFlow = combine(
            prayerTimesFlow,
            timeTickFlow
        ) { times, _ ->
            times?.let { getNextPrayerUseCase.getNextPrayer(it, clock.currentMinutesOfDay()) }
        }

        planJob?.cancel()
        planJob = viewModelScope.launch {
            combine(
                nextPrayerFlow,
                userPreferencesRepository.preferences
            ) { nextPrayer, prefs ->
                nextPrayer to prefs
            }.onEach { (next, prefs) ->
                if (next != null && prefs != null) {
                    val context = UserContext(
                        availableMinutes = prefs.maxSessionMinutes.coerceAtLeast(5),
                        energyLevel = EnergyLevel.MEDIUM,
                        preferences = prefs,
                        currentTimeMinutes = clock.currentMinutesOfDay(),
                        nextPrayer = next.name,
                        ramadanDay = ramadanDayProvider.getRamadanDay()
                    )
                    try {
                        cachedPlan.value = generateTodayPlanUseCase(context)
                    } catch (_: Exception) {
                        cachedPlan.value = com.ramadan.companion.domain.plan.model.TodayPlan(
                            title = "A calm moment",
                            subtitle = "You have ${prefs.maxSessionMinutes} minutes.",
                            suggestedActions = listOf("5 min dhikr", "Read a page of Quran")
                        )
                    }
                }
            }.launchIn(this)
        }

        combine(
            nextPrayerFlow,
            prayerTimesFlow,
            cachedPlan,
            userPreferencesRepository.preferences,
            userPreferencesDataSource.userName
        ) { nextPrayer, prayerTimes, plan, _, userName ->
            val progress = prayerTimes?.let { computeProgress(it, clock.currentMinutesOfDay()) } ?: 0f
            buildState(
                nextPrayer = nextPrayer,
                plan = plan,
                userName = userName,
                progressPercent = progress
            )
        }.onEach { newState ->
            _state.update { _ -> newState }
        }.launchIn(viewModelScope)
    }

    private fun buildState(
        nextPrayer: NextPrayerInfo?,
        plan: com.ramadan.companion.domain.plan.model.TodayPlan?,
        userName: String,
        progressPercent: Float
    ): TodayUiState {
        val nextPrayerStr = nextPrayer?.let { "${it.name} • ${it.countdownText}" } ?: ""
        val suggestedIbadahStr = plan?.let { "${it.subtitle}\n${it.suggestedActions.joinToString(" • ")}" } ?: ""
        return TodayUiState(
            isLoading = false,
            errorMessage = _state.value.errorMessage,
            userName = userName,
            ramadanDay = ramadanDayProvider.getRamadanDay(),
            progressPercent = progressPercent,
            nextPrayer = nextPrayerStr,
            suggestedIbadah = suggestedIbadahStr,
            quickActions = defaultQuickActions()
        )
    }

    private fun computeProgress(prayerTimes: com.ramadan.companion.domain.prayer.model.PrayerTimes, currentMinutes: Int): Float {
        val start = prayerTimes.fajr
        val end = prayerTimes.isha
        if (end <= start) return 0f
        val progress = (currentMinutes - start).toFloat() / (end - start)
        return progress.coerceIn(0f, 1f)
    }

    private fun defaultQuickActions(): List<QuickAction> = listOf(
        QuickAction("dhikr", "Quick Dhikr", false),
        QuickAction("energy", "Energy Check", false),
        QuickAction("ai", "Ask AI", true)
    )

    private fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                prayerTimeRepository.refreshPrayerTimes()
                cachedPlan.value = null
            } catch (_: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = "Could not refresh") }
            }
        }
    }

    private fun onQuickActionClicked(actionId: String) {
        viewModelScope.launch {
            when (actionId) {
                "ai" -> _events.emit(TodaySideEffect.NavigateToCompanion)
                else -> { }
            }
        }
    }
}

sealed interface TodaySideEffect {
    data object NavigateToCompanion : TodaySideEffect
}
