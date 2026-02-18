# Ramadan Companion AI — Android

Production-grade Android app built with **Kotlin**, **Jetpack Compose**, **Material 3**, and **modular Clean Architecture**.

## Quick start

1. Open the `android` folder in **Android Studio** (Ladybug or newer).
2. Sync Gradle (Android Studio will download the Gradle wrapper if needed).
3. Run on a device or emulator (API 26+).

If you use the command line and already have the wrapper:

```bash
cd android
./gradlew :app:assembleDebug
```

To generate the Gradle wrapper (if missing):

```bash
gradle wrapper --gradle-version=8.9
```

## Project structure

| Module | Purpose |
|--------|---------|
| **:app** | Entry point, `MainActivity`, `RamadanNavHost`, Hilt `Application` |
| **:core:ui** | Bottom bar, navigation routes |
| **:core:designsystem** | Theme (Ramadan navy/gold), typography, `RamadanCard`, `ProgressRing` |
| **:core:network** | Retrofit, OkHttp (for future AI API) |
| **:core:database** | DataStore preferences, Hilt `DatabaseModule` |
| **:domain** | Use cases, domain models, repository interfaces |
| **:feature:today** | Today dashboard — full clean-arch flow (data → domain → presentation) |
| **:feature:companion** | AI Companion chat (shell) |
| **:feature:quran** | Quran & Dhikr (shell) |
| **:feature:reflection** | Reflection & growth (shell) |

## Architecture

See **[ARCHITECTURE.md](ARCHITECTURE.md)** for:

- Layer structure (Presentation, Domain, Data)
- State management (UDF, StateFlow, sealed events)
- Example: Today screen end-to-end flow
- Design system and navigation

## Design

- **Theme**: Deep navy (`#0D1B3D`, `#0a0f24`) and gold (`#E3B96B`) to match the Figma/React design reference.
- **Components**: Rounded cards, progress ring, bottom nav with gold accent.
- **Dynamic dark**: App uses a single dark theme; system dark/light can be extended later.

## Tech stack

- Kotlin 2.0, Jetpack Compose, Material 3  
- Kotlin Coroutines + Flow, StateFlow  
- Hilt, Navigation Compose  
- DataStore (preferences), Room (ready for use)  
- Retrofit + OkHttp, Kotlin Serialization (for AI API)  
- Coil (for images when needed)

## Screens

1. **Today** — Greeting, progress ring, next prayer, suggested ibadah, quick action chips (full example with ViewModel + use case + repository).
2. **AI Companion** — Placeholder; ready for chat UI and streaming AI.
3. **Quran** — Placeholder; ready for Arabic + translation, audio, bookmarks.
4. **Reflection** — Placeholder; ready for gratitude input, AI insight, growth visualization.

Build and run from Android Studio or `./gradlew :app:assembleDebug` to try the app.
