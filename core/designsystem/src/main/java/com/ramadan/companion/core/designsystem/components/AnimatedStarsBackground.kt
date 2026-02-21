package com.ramadan.companion.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.ramadan.companion.core.designsystem.theme.RamadanColors

/**
 * Lightweight animated star layer for Today and Companion screens.
 * Uses fixed star positions (remember) and a single infinite transition
 * to drive vertical float + alpha so Canvas does not recompose on content changes.
 * Opacity kept low (0.05–0.1) so it does not block UI or distract.
 */
@Composable
fun AnimatedStarsBackground(
    modifier: Modifier = Modifier,
    starColor: Color = RamadanColors.Gold,
    maxAlpha: Float = 0.08f,
    starCount: Int = 32
) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val starData = remember(starCount) {
        List(starCount) { i ->
            StarSpec(
                xFraction = seededRandom(i * 3).toFloat(),
                yFraction = seededRandom(i * 3 + 1).toFloat(),
                radiusPx = if (i % 3 == 0) 1.5f else 1f,
                driftAmplitude = 4f + seededRandom(i * 3 + 2).toFloat() * 8f,
                alphaBase = 0.03f + seededRandom(i).toFloat() * (maxAlpha - 0.03f)
            )
        }
    }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 1f }
    ) {
        val w = size.width
        val h = size.height
        starData.forEach { spec ->
            val yOffset = spec.driftAmplitude * (2f * (phase - 0.5f))
            val alphaShimmer = 0.7f + 0.3f * (0.5f + 0.5f * kotlin.math.sin(shimmer * 2 * kotlin.math.PI + spec.xFraction * 10).toFloat())
            val x = spec.xFraction * w
            val y = (spec.yFraction * h + yOffset).let { if (it < 0) it + h else if (it > h) it - h else it }
            drawCircle(
                color = starColor.copy(alpha = (spec.alphaBase * alphaShimmer).coerceIn(0f, 1f)),
                radius = spec.radiusPx,
                center = Offset(x, y)
            )
        }
    }
}

private data class StarSpec(
    val xFraction: Float,
    val yFraction: Float,
    val radiusPx: Float,
    val driftAmplitude: Float,
    val alphaBase: Float
)

/**
 * Subtle star layer with slow upward float and gentle alpha fade.
 * Use Modifier.fillMaxSize() layered behind content on TodayScreen.
 * Alpha 0.05–0.1; does not block interaction.
 */
@Composable
fun FadingStarsBackground(
    modifier: Modifier = Modifier,
    starColor: Color = RamadanColors.Gold,
    maxAlpha: Float = 0.07f,
    starCount: Int = 28
) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val fadePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val starData = remember(starCount) {
        List(starCount) { i ->
            StarSpec(
                xFraction = seededRandom(i * 5).toFloat(),
                yFraction = seededRandom(i * 5 + 1).toFloat(),
                radiusPx = if (i % 4 == 0) 1.5f else 1f,
                driftAmplitude = 0f,
                alphaBase = 0.02f + seededRandom(i).toFloat() * (maxAlpha - 0.02f)
            )
        }
    }
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 1f }
    ) {
        val w = size.width
        val h = size.height
        val upwardDrift = h * 0.15f
        starData.forEach { spec ->
            val yRaw = spec.yFraction * h - phase * upwardDrift
            val y = when {
                yRaw < 0 -> yRaw + h
                yRaw > h -> yRaw - h
                else -> yRaw
            }
            val alphaFade = 0.5f + 0.5f * kotlin.math.sin(fadePhase * 2 * kotlin.math.PI + spec.xFraction * 6).toFloat()
            val x = spec.xFraction * w
            drawCircle(
                color = starColor.copy(
                    alpha = (spec.alphaBase * alphaFade).coerceIn(0f, 1f)
                ),
                radius = spec.radiusPx,
                center = Offset(x, y)
            )
        }
    }
}

private fun seededRandom(seed: Int): Double {
    var s = seed.toLong() and 0xFFFFFFFFL
    s = (s * 1103515245 + 12345) and 0x7FFFFFFFL
    return s.toDouble() / 0x7FFFFFFFL
}
