# Ramadan Companion AI — Architecture

## Overview

Production-grade Android app using **modular Clean Architecture** with clear separation of concerns. The app is Compose-first, uses Kotlin Coroutines + Flow for reactive data, and Hilt for dependency injection.

---

## 1. Project Structure

```
android/
├── app/                    # Application module (entry point, theme, nav host)
├── core/
│   ├── ui/                  # Shared UI: bottom bar, navigation routes
│   ├── designsystem/        # Theme (Ramadan colors, typography), reusable components
│   ├── network/             # Retrofit, OkHttp, API contracts (for future AI API)
│   └── database/            # DataStore preferences, Room (when needed)
├── domain/                  # Business logic: use cases, domain models, repository interfaces
└── feature/
    ├── today/               # Today dashboard (full clean-arch example)
    ├── companion/           # AI Companion chat
    ├── quran/               # Quran & Dhikr
    └── reflection/          # Reflection & growth
```

---

## 2. Layer Structure

### Presentation Layer (per feature)

- **Compose screens**: UI only; read from `StateFlow<UiState>` and send `Event`s to ViewModel.
- **ViewModels**: Single responsibility; expose `StateFlow<UiState>`, handle `Event`s, call use cases.
- **UI State**: Immutable data classes; single source of truth for the screen.
- **Events**: Sealed interface for user actions; side effects (navigation, toasts) via `SharedFlow` or callbacks.

### Domain Layer

- **Use cases**: One per action (e.g. `GetTodayStateUseCase`); depend only on repository interfaces.
- **Models**: Immutable domain entities; no Android or framework dependencies.
- **Repository interfaces**: Defined in `domain`; implemented in feature or data modules.

### Data Layer

- **Repository implementations**: Live in feature modules (e.g. `feature:today`) or a dedicated `:data` module; implement domain interfaces.
- **Data sources**: Local (DataStore, Room) and remote (Retrofit); offline-first where applicable.
- **Dependency direction**: Data → Domain (domain has no dependency on data).

---

## 3. State Management (UDF)

- **Unidirectional Data Flow**: User → Event → ViewModel → Use case / Repository → StateFlow → UI.
- **Immutable UI state**: `data class TodayUiState(...)`; ViewModel uses `MutableStateFlow.update { }`.
- **Sealed events**: `TodayEvent.Refresh`, `TodayEvent.QuickActionClicked(id)`.
- **Side effects**: Handled via `SharedFlow<TodaySideEffect>` (e.g. navigate to Companion) or callback from the screen.

---

## 4. Dependency Flow

- **:app** → :core:ui, :core:designsystem, :feature:*
- **:feature:today** → :domain, :core:ui, :core:designsystem, :core:database
- **:domain** → (no Android; only Kotlin + Coroutines)
- **:core:database** → DataStore, Hilt (provides `UserPreferencesDataSource`)
- **:core:network** → Retrofit, OkHttp, Kotlin Serialization (for future AI API)

Dependency inversion: features and app depend on `domain`; data layer implements domain interfaces.

---

## 5. Design System

- **Theme**: `RamadanCompanionTheme` in `:core:designsystem` (Material 3 dark color scheme).
- **Colors**: `RamadanColors` — deep navy (`NavyPrimary`, `NavyDeep`), gold (`Gold`, `GoldDark`), purple accents (`PurpleAccent`), text (`TextPrimary`, `TextSecondary`).
- **Components**: `RamadanCard`, `ProgressRing`; reusable across features.
- **Typography**: `RamadanTypography` (headline, body, label).

---

## 6. Navigation

- **Routes**: Centralized in `:core:ui` as `RamadanRoutes` (today, companion, quran, reflection).
- **NavHost**: In `:app`; composables for each route; `TodayScreen` gets `TodayViewModel` via `hiltViewModel()`.
- **Bottom bar**: `RamadanBottomBar` in `:core:ui`; highlights current route and navigates on tab click.

---

## 7. Example: Today Screen (Full Flow)

1. **Domain**
   - `TodayState`, `PrayerInfo`, `SuggestedIbadah`, `DayTimeline`, `QuickAction` (immutable models).
   - `TodayRepository` interface: `getTodayState(): Flow<TodayState>`, `refreshTodayState()`.
   - `GetTodayStateUseCase`: invokes `repository.getTodayState()`.

2. **Data**
   - `TodayRepositoryImpl` in `:feature:today`: uses `UserPreferencesDataSource` (DataStore) and builds `TodayState` (currently with placeholder data).
   - Bound in Hilt via `TodayModule` (`TodayRepository` → `TodayRepositoryImpl`).

3. **Presentation**
   - `TodayViewModel`: injects `GetTodayStateUseCase`; collects flow and maps to `TodayUiState`; handles `TodayEvent` and emits `TodaySideEffect` (e.g. navigate to Companion).
   - `TodayUiState`: data class for UI (progress, next prayer, suggested ibadah, quick actions).
   - `TodayScreen`: Composes UI from state, sends events to ViewModel, observes side effects and calls `onNavigateToCompanion()`.

---

## 8. Scalability & Quality

- **Feature isolation**: Each feature module can be developed and tested independently.
- **Preview-friendly composables**: Pass `UiState` and lambda for events; no ViewModel in previews.
- **Testability**: ViewModels and use cases are unit-testable with mocked repositories.
- **Offline-first**: Repository exposes `Flow`; local sources are primary; remote can be added behind the same interface.

---

## 9. Tech Stack Summary

| Concern           | Technology                |
|------------------|---------------------------|
| UI               | Jetpack Compose, Material 3 |
| DI               | Hilt                      |
| Async / state    | Kotlin Coroutines, Flow, StateFlow |
| Local persistence| DataStore (prefs), Room (when needed) |
| Network          | Retrofit, OkHttp (for AI API) |
| Images           | Coil (when needed)        |
| Serialization    | Kotlin Serialization      |
| Navigation       | Navigation Compose        |

---

## 10. Next Steps

- Add Room entities and DAOs in `:core:database` for bookmarks, reflections, chat history.
- Implement AI Companion screen with Retrofit API and streaming response handling.
- Add prayer times (local or API) and replace placeholder `TodayState` in `TodayRepositoryImpl`.
- Add unit tests for ViewModels and use cases; UI tests for critical flows.
