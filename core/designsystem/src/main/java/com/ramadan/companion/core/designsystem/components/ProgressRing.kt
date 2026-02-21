package com.ramadan.companion.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.theme.DesignSystemTypography
import com.ramadan.companion.core.designsystem.theme.RamadanColors

/**
 * Premium progress circle with zoom-in on first composition, subtle breathing
 * loop, and smooth arc animation. Center content uses graphicsLayer for scale
 * to avoid recomposition storm.
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
    val targetProgress = progress.coerceIn(0f, 1f)
    val arcAnimatable = remember { Animatable(0f) }
    val scaleAnimatable = remember { Animatable(0.85f) }

    // Arc: animate from 0 to target independently
    LaunchedEffect(targetProgress) {
        arcAnimatable.snapTo(0f)
        arcAnimatable.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(durationMillis = 900)
        )
    }

    // Zoom-in on first composition, then start breathing
    LaunchedEffect(Unit) {
        scaleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        // Infinite breathing: 1f <-> 1.03f, subtle
        while (true) {
            scaleAnimatable.animateTo(
                targetValue = 1.03f,
                animationSpec = tween(durationMillis = 2200)
            )
            scaleAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2200)
            )
        }
    }

    val animatedProgress = arcAnimatable.value
    val scale = scaleAnimatable.value

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val radius = (this.size.width - strokeWidth.toPx()) / 2f
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
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
        Column(
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = DesignSystemTypography.heroGreeting,
                color = RamadanColors.Gold
            )
            Text(
                text = label,
                style = DesignSystemTypography.caption,
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
