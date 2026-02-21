# Animation architecture (design system)

Short explanation of how animations are structured for the Today and Companion screens so they stay **premium, calm, and performant**.

## 1. Progress circle (zoom + breath + arc)

- **Zoom-in on first composition**: `LaunchedEffect(Unit)` runs once. Scale starts at `0.85f` and animates to `1f` with a **spring** (medium bouncy, low stiffness) so the circle lands softly.
- **Breathing loop**: After the zoom finishes, the same `Animatable` is driven in an infinite loop between `1f` and `1.03f` with **tween** (2.2s each way). Only the center content (percentage + label) is scaled via **`graphicsLayer { scaleX = scale; scaleY = scale }`** so the circle layout doesn’t recompose; only the GPU transform updates.
- **Arc**: A **separate** `Animatable` drives the arc from `0f` to the target progress with a 900ms tween. Arc and scale are independent: arc can animate while the circle is already breathing.
- **Why**: One-shot zoom for entrance, then a subtle breath so the circle feels alive without being distracting. `graphicsLayer` keeps animation off the composition path.

## 2. Animated / Fading stars background

- **AnimatedStarsBackground** and **FadingStarsBackground**: Single **Canvas** with a fixed list of star specs (positions, radius, alpha) created in **`remember(starCount)`** so star positions don’t change across recompositions.
- **One `rememberInfiniteTransition`** per composable drives:
  - **Phase**: vertical drift (AnimatedStarsBackground) or upward float (FadingStarsBackground, y decreases over time, wrapping).
  - **Shimmer / fadePhase**: subtle alpha variation so stars gently fade in/out.
- Stars are drawn in one `Canvas`; the only recomposition comes from the transition values. No per-star composables, so no recomposition storm. Star layer is **outside** any LazyColumn; it sits behind content in a Box so the screen doesn’t recompose with star animation.
- **Opacity** is kept low (0.05–0.1) so the layer stays decorative and doesn’t block interaction or readability.
- **Why**: One draw pass, one transition, no nested infinite transitions. Feels alive but calm.

## 2b. PrayerTimelineCard (Now dot)

- **Single `rememberInfiniteTransition`** drives **pulseAlpha** (0.6f ↔ 1f) and **pulseScale** (0.92f ↔ 1.08f) with **tween** (1.4s / 1.6s, Reverse). Center dot uses **`graphicsLayer { scaleX/Y = pulseScale }`** and **alpha = pulseAlpha** for a subtle pulse; no harsh blinking.
- Card uses vertical gradient and soft overlay; no extra infinite loops.

## 2c. Bottom nav icon

- **animateFloatAsState** for scale (1f → 1.1f when selected, 300ms tween). **animateColorAsState** for tint (gold vs dimmed). Icon wrapped in **`graphicsLayer { scaleX = scale; scaleY = scale }`** so scale doesn’t trigger layout recomposition. Smooth and calm; no bounce.

## 3. Companion screen (messages + avatar)

- **AI messages**: Wrapped in **`AnimatedVisibility`** with **`fadeIn() + slideInVertically(initialOffsetY = 8.dp)`** so assistant messages appear with a short fade and slight upward slide (8dp), not instant.
- **Avatar**: A **radial gradient** circle (gold → transparent) is drawn **behind** the avatar to create a soft glow. No extra animation on the glow; the existing ring rotation remains.
- **Why**: Small, predictable enter animations and a static glow keep the companion feeling polished and calm without adding heavy animation logic.

## 4. General rules

- **Extract animated UI** into composables (e.g. `ProgressCircle`, `AnimatedStarsBackground`) so parent screens don’t recompose when animation state changes.
- **Use `graphicsLayer`** for scale/alpha/translation where possible so work stays on the GPU and doesn’t trigger full recomposition.
- **Use `remember` / `remember(keys)`** for stable data (star positions, initial values) so animations don’t restart unnecessarily.
- **Single `rememberInfiniteTransition`** for many “ambient” values (e.g. stars) to avoid multiple infinite animations and keep one source of truth.
- **No animation inside large recomposing scopes** (e.g. inside list item lambdas that recompose often); keep animation in small, stable composables.

Result: animations feel smooth and consistent, stay off the main composition path where possible, and keep the app feeling like a top-tier wellness experience without sacrificing performance.
