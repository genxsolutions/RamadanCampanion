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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.components.ProgressCircle
import com.ramadan.companion.core.designsystem.components.RamadanCard
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.RamadanTypography

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
        modifier = modifier
    )
}

@Composable
private fun TodayContent(
    state: TodayUiState,
    onEvent: (TodayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = RamadanColors.Gold)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        AnimatedVisibility(
            visible = !state.isLoading,
            enter = fadeIn() + slideInVertically { it / 4 }
        ) {
            Column {
                Text(
                    text = "Assalamu Alaikum, ${state.userName.ifEmpty { "Guest" }} \uD83C\uDF19",
                    style = RamadanTypography.headlineMedium,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = "Day ${state.ramadanDay} of Ramadan",
                    style = RamadanTypography.bodyMedium,
                    color = RamadanColors.Gold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Centered progress circle
        AnimatedVisibility(
            visible = !state.isLoading,
            enter = fadeIn() + slideInVertically { it / 4 }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ProgressCircle(
                    progress = state.progressPercent,
                    label = "Today\'s Progress"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader(
            title = "Today\'s Flow",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (state.nextPrayer.isNotBlank()) {
            RamadanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = null
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = RamadanColors.Gold
                        )
                        Column {
                            Text(
                                text = "Next Prayer",
                                style = RamadanTypography.labelSmall,
                                color = RamadanColors.Gold.copy(alpha = 0.7f)
                            )
                            Text(
                                text = state.nextPrayer,
                                style = RamadanTypography.titleLarge,
                                color = RamadanColors.TextPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.suggestedIbadah.isNotBlank()) {
            RamadanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = null
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = RamadanColors.Gold
                    )
                    Column {
                        Text(
                            text = "Suggested Ibadah",
                            style = RamadanTypography.bodyMedium,
                            color = RamadanColors.Gold,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = state.suggestedIbadah,
                            style = RamadanTypography.bodySmall,
                            color = RamadanColors.TextPrimary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.quickActions.forEach { action ->
                ActionChip(
                    text = action.label,
                    isPrimary = action.isPrimary,
                    onClick = { onEvent(TodayEvent.QuickActionClicked(action.id)) }
                )
            }
        }
    }
}

@Composable
private fun ActionChip(
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isPrimary) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.1f)
    val textColor = if (isPrimary) RamadanColors.NavyPrimary else RamadanColors.Gold
    Surface(
        onClick = onClick,
        modifier = modifier.wrapContentWidth(),
        shape = androidx.compose.foundation.shape.CircleShape,
        color = backgroundColor,
        tonalElevation = if (isPrimary) 4.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isPrimary) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                text = text,
                color = textColor
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = RamadanTypography.titleMedium,
        color = RamadanColors.TextPrimary,
        modifier = modifier
    )
}
