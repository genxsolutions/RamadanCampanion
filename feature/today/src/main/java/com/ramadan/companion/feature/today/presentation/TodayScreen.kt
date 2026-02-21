package com.ramadan.companion.feature.today.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramadan.companion.core.designsystem.components.FadingStarsBackground
import com.ramadan.companion.core.designsystem.components.PrayerTimelineCard
import com.ramadan.companion.core.designsystem.components.ProgressCircle
import com.ramadan.companion.core.designsystem.components.RamadanCard
import com.ramadan.companion.core.designsystem.components.RamadanCardGradientFirst
import com.ramadan.companion.core.designsystem.components.RamadanCardGradientSecond
import com.ramadan.companion.core.designsystem.theme.DesignSystemTypography
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.Spacing

@Composable
fun TodayScreen(
    viewModel: TodayViewModel,
    onNavigateToCompanion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { effect ->
            when (effect) {
                is TodaySideEffect.NavigateToCompanion -> onNavigateToCompanion()
            }
        }
    }

    TodayContent(
        state = state,
        onEvent = viewModel::handleEvent,
        onNavigateToCompanion = onNavigateToCompanion,
        modifier = modifier
    )
}

@Composable
private fun TodayContent(
    state: TodayUiState,
    onEvent: (TodayEvent) -> Unit,
    onNavigateToCompanion: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(RamadanColors.NavyPrimary),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = RamadanColors.Gold)
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        RamadanColors.NavyDeep,
                        RamadanColors.NavyPrimary,
                        RamadanColors.NavyVariant
                    )
                )
            )
    ) {
        FadingStarsBackground(
            modifier = Modifier.fillMaxSize(),
            maxAlpha = 0.07f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .drawWithContent {
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.12f)),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = maxOf(size.width, size.height)
                        )
                    )
                    drawContent()
                }
                .padding(horizontal = Spacing.lg, vertical = Spacing.lg)
        ) {
            // Header: Assalamu Alaikum + Day of Ramadan
            AnimatedVisibility(
                visible = !state.isLoading,
                enter = fadeIn() + slideInVertically { it / 4 }
            ) {
                Column {
                    Text(
                        text = "Assalamu Alaikum, ${state.userName.ifEmpty { "Guest" }} \uD83C\uDF19",
                        style = DesignSystemTypography.heroGreeting,
                        color = RamadanColors.TextPrimary
                    )
                    Text(
                        text = "Day ${state.ramadanDay} of Ramadan",
                        style = DesignSystemTypography.ramadanDayText,
                        color = RamadanColors.Gold,
                        modifier = Modifier.padding(top = Spacing.xxs)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.lg))

            // Progress Circle (centered, with radial glow behind)
            AnimatedVisibility(
                visible = !state.isLoading,
                enter = fadeIn() + slideInVertically { it / 4 }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawWithContent {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        RamadanColors.PurpleAccent.copy(alpha = 0.2f),
                                        Color.Transparent
                                    ),
                                    center = Offset(size.width / 2f, size.height / 2f),
                                    radius = 140f
                                )
                            )
                            drawContent()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    ProgressCircle(
                        progress = state.progressPercent,
                        label = "Today's Progress"
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xl))

            // Section: Today's Flow
            Text(
                text = "Today's Flow",
                style = DesignSystemTypography.sectionHeader,
                color = RamadanColors.TextPrimary,
                modifier = Modifier.padding(bottom = Spacing.md)
            )

            // Card 1: Next Prayer — title = prayer name only; countdown on second line
            if (state.nextPrayer.isNotBlank()) {
                val prayerName = state.nextPrayer.substringBefore(" in ").trim().ifEmpty { state.nextPrayer }
                RamadanCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 88.dp),
                    onClick = null,
                    gradient = RamadanCardGradientFirst,
                    cornerRadius = 24.dp,
                    contentPadding = 20.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(RamadanColors.PurpleAccent.copy(alpha = 0.5f))
                                .padding(Spacing.xs),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = RamadanColors.Gold
                            )
                        }
                        Column {
                            Text(
                                text = "Next Prayer",
                                style = DesignSystemTypography.smallLabel,
                                color = RamadanColors.Gold.copy(alpha = 0.9f)
                            )
                            Text(
                                text = prayerName,
                                style = DesignSystemTypography.heroGreeting.copy(
                                    fontSize = 22.sp,
                                    lineHeight = 28.sp
                                ),
                                color = RamadanColors.TextPrimary
                            )
                            Text(
                                text = "in 2 hours 45 minutes",
                                style = DesignSystemTypography.smallLabel,
                                color = RamadanColors.TextSecondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Card 2: Suggested Ibadah (match web: book icon, title, bullet list)
            if (state.suggestedIbadah.isNotBlank()) {
                RamadanCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 88.dp),
                    onClick = null,
                    gradient = RamadanCardGradientSecond,
                    cornerRadius = 24.dp,
                    contentPadding = 20.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(RamadanColors.PurpleAccent.copy(alpha = 0.5f))
                                .padding(Spacing.xs),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = RamadanColors.Gold
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "You have 15 calm minutes before work",
                                style = DesignSystemTypography.cardTitle,
                                color = RamadanColors.TextPrimary
                            )
                            BulletList(
                                items = state.suggestedIbadah.split("\n").filter { it.isNotBlank() }
                                    .ifEmpty { listOf(state.suggestedIbadah) },
                                modifier = Modifier.padding(top = Spacing.xs)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.xl))
            }

            // Prayer Timeline (match web: Suhoor ✓ — Now ● — Iftar ○ with times)
            AnimatedVisibility(
                visible = !state.isLoading,
                enter = fadeIn() + slideInVertically { it / 4 }
            ) {
                PrayerTimelineCard(
                    suhoorLabel = "Suhoor",
                    suhoorTime = "4:30 AM",
                    nowTime = "3:15 PM",
                    iftarLabel = "Iftar",
                    iftarTime = "6:42 PM"
                )
            }

            Spacer(modifier = Modifier.height(Spacing.lg))

            // Quick Dhikr & Energy Check: dark purple/navy gradient, pill-shaped, horizontally aligned
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.quickActions.filter { !it.isPrimary }.forEach { action ->
                    ActionChip(
                        text = action.label,
                        onClick = { onEvent(TodayEvent.QuickActionClicked(action.id)) }
                    )
                }
            }

            // Ask AI: larger button, central, distinct spacing, gold gradient, white text
            Spacer(modifier = Modifier.height(Spacing.lg))
            val askAiAction = state.quickActions.find { it.isPrimary }
            AskAiButton(
                text = askAiAction?.label ?: "Ask AI",
                onClick = {
                    askAiAction?.let { onEvent(TodayEvent.QuickActionClicked(it.id)) }
                    onNavigateToCompanion()
                }
            )
        }
    }
}

@Composable
private fun BulletList(
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(RamadanColors.Gold, CircleShape)
                )
                Text(
                    text = item.trim(),
                    style = DesignSystemTypography.cardSubtitle,
                    color = RamadanColors.TextPrimary
                )
            }
        }
    }
}

@Composable
private fun ActionChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.wrapContentWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            RamadanColors.PurpleAccent.copy(alpha = 0.6f),
                            RamadanColors.PurpleAccentDark.copy(alpha = 0.5f)
                        )
                    ),
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = Spacing.lg, vertical = Spacing.sm)
        ) {
            Text(
                text = text,
                style = DesignSystemTypography.cardSubtitle,
                color = RamadanColors.TextPrimary
            )
        }
    }
}

@Composable
private fun AskAiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            RamadanColors.Gold.copy(alpha = 0.95f),
                            RamadanColors.GoldDark.copy(alpha = 0.9f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = Spacing.lg, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = RamadanColors.NavyPrimary
            )
            Spacer(modifier = Modifier.size(Spacing.sm))
            Text(
                text = text,
                style = DesignSystemTypography.cardTitle,
                color = RamadanColors.NavyPrimary
            )
        }
    }
}
