package com.ramadan.companion.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.RamadanTypography

/**
 * Reusable circular progress component for the Today screen.
 * Animates from 0 to [progress] every time the composable enters composition
 * (first launch or when navigating back to the Today tab).
 * Visually centered when wrapped in a Box with contentAlignment = Center.
 */
@Composable
fun ProgressCircle(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    strokeWidth: Dp = 6.dp,
    progressColor: Color = RamadanColors.Gold,
    trackColor: Color = RamadanColors.PurpleAccent.copy(alpha = 0.4f),
    label: String = "Today's Progress"
) {
    val animatable = remember { Animatable(0f) }
    val targetProgress = progress.coerceIn(0f, 1f)

    LaunchedEffect(targetProgress) {
        animatable.snapTo(0f)
        animatable.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(durationMillis = 900)
        )
    }
    val animatedProgress = animatable.value

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val radius = (size.toPx() - strokeWidth.toPx()) / 2f
            val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
            rotate(-90f) {
                drawCircle(
                    color = trackColor,
                    radius = radius,
                    center = center,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
                drawArc(
                    color = progressColor,
                    startAngle = 0f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = RamadanTypography.headlineLarge,
                color = RamadanColors.Gold
            )
            Text(
                text = label,
                style = RamadanTypography.labelSmall,
                color = RamadanColors.TextSecondary
            )
        }
    }
}

// Backwards-compatible alias if other modules were using ProgressRing.
@Deprecated("Use ProgressCircle instead", replaceWith = ReplaceWith("ProgressCircle(progress, modifier, size, strokeWidth, progressColor, trackColor, label)"))
@Composable
fun ProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    strokeWidth: Dp = 6.dp,
    progressColor: Color = RamadanColors.Gold,
    trackColor: Color = RamadanColors.PurpleAccent.copy(alpha = 0.4f),
    label: String = "Today's Progress"
) = ProgressCircle(
    progress = progress,
    modifier = modifier,
    size = size,
    strokeWidth = strokeWidth,
    progressColor = progressColor,
    trackColor = trackColor,
    label = label
)
