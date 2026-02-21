package com.ramadan.companion.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.theme.DesignSystemTypography
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.Spacing

/**
 * Timeline card matching web: [ Suhoor ✓ ] —— ● Now —— [ Iftar ○ ].
 * Suhoor = checkmark in dark circle; Now = gold pulsing dot with glow; Iftar = outline circle.
 * Optional times under each label (e.g. "4:30 AM", "3:15 PM", "6:42 PM").
 */
@Composable
fun PrayerTimelineCard(
    suhoorLabel: String = "Suhoor",
    suhoorTime: String? = null,
    nowTime: String? = null,
    iftarLabel: String = "Iftar",
    iftarTime: String? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(
            RamadanColors.PurpleAccent.copy(alpha = 0.7f),
            RamadanColors.PurpleAccentDark.copy(alpha = 0.5f),
            RamadanColors.NavyCard.copy(alpha = 0.9f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RamadanColors.OverlayGold.copy(alpha = 0.08f)
                        )
                    )
                )
            }
            .padding(horizontal = Spacing.md, vertical = Spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Suhoor: checkmark in dark circle + label + time
            TimelineSegment(
                label = suhoorLabel,
                time = suhoorTime,
                state = TimelineSegmentState.Completed
            )

            // Left line (Suhoor → Now): gold accent for current segment
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .height(2.dp)
                    .background(
                        RamadanColors.Gold.copy(alpha = 0.4f),
                        RoundedCornerShape(1.dp)
                    )
            )

            // Now: gold dot with glow + label + time
            Column(
                modifier = Modifier.weight(1.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                        .drawBehind {
                            drawCircle(
                                color = RamadanColors.Gold.copy(alpha = 0.25f),
                                radius = 24.dp.toPx(),
                                center = Offset(size.width / 2f, size.height / 2f)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(RamadanColors.Gold.copy(alpha = pulseAlpha))
                    )
                }
                Text(
                    text = "Now",
                    style = DesignSystemTypography.cardSubtitle,
                    color = RamadanColors.Gold,
                    modifier = Modifier.padding(top = Spacing.xxs)
                )
                if (nowTime != null) {
                    Text(
                        text = nowTime,
                        style = DesignSystemTypography.smallLabel,
                        color = RamadanColors.Gold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Right line (Now → Iftar): lighter grey
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .height(2.dp)
                    .background(
                        RamadanColors.BorderGold.copy(alpha = 0.25f),
                        RoundedCornerShape(1.dp)
                    )
            )

            // Iftar: outline circle + label + time
            TimelineSegment(
                label = iftarLabel,
                time = iftarTime,
                state = TimelineSegmentState.Upcoming
            )
        }
    }
}

private enum class TimelineSegmentState { Completed, Current, Upcoming }

@Composable
private fun RowScope.TimelineSegment(
    label: String,
    time: String?,
    state: TimelineSegmentState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            TimelineSegmentState.Completed -> {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(RamadanColors.NavyCard)
                        .border(1.dp, RamadanColors.BorderGold.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = RamadanColors.Gold
                    )
                }
            }
            TimelineSegmentState.Upcoming -> {
                // Iftar: empty circle — dark background with faint gold outline (pending state)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(RamadanColors.NavyCard)
                        .border(1.dp, RamadanColors.BorderGold.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {}
            }
            TimelineSegmentState.Current -> { /* handled in main Row */ }
        }
        Text(
            text = label,
            style = DesignSystemTypography.cardSubtitle,
            color = RamadanColors.TextPrimary,
            modifier = Modifier.padding(top = Spacing.xxs)
        )
        if (time != null) {
            Text(
                text = time,
                style = DesignSystemTypography.smallLabel,
                color = RamadanColors.TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
