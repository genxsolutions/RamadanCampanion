package com.ramadan.companion.feature.companion.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramadan.companion.core.designsystem.components.AnimatedStarsBackground
import com.ramadan.companion.core.designsystem.components.RamadanCard
import com.ramadan.companion.core.designsystem.theme.DesignSystemTypography
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.Spacing

private data class SuggestionCardUi(
    val title: String,
    val subtitle: String,
    val icon: @Composable () -> Unit
)

@Composable
fun CompanionScreen(
    onUpClick: () -> Unit = {},
    viewModel: CompanionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val messages = state.messages
    val suggestions = rememberSuggestionCards()
    val quickPrompts = listOf(
        "5-minute ibadah plan",
        "Dua suggestion",
        "Gentle motivation",
        "What should I focus on?"
    )
    val scrollState = rememberScrollState()

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
        AnimatedStarsBackground(
            modifier = Modifier.fillMaxSize(),
            maxAlpha = 0.07f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header with avatar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompanionAvatar()
                Spacer(modifier = Modifier.height(Spacing.md))
                Text(
                    text = "Your AI Companion",
                    style = DesignSystemTypography.heroGreeting,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = "Here to guide your day",
                    style = DesignSystemTypography.caption,
                    color = RamadanColors.Gold.copy(alpha = 0.7f)
                )
            }

            // Chat messages
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                messages.forEach { msg ->
                    if (msg.isFromUser) {
                        ChatBubble(
                            text = msg.text,
                            time = msg.time,
                            isFromUser = true
                        )
                    } else {
                        val slidePx = with(LocalDensity.current) { 8.dp.roundToPx() }
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically(initialOffsetY = { slidePx })
                        ) {
                            ChatBubble(
                                text = msg.text,
                                time = msg.time,
                                isFromUser = false
                            )
                        }
                    }
                }

                // Suggestion cards
                Text(
                    text = "Suggestions for you:",
                    style = DesignSystemTypography.caption,
                    color = RamadanColors.Gold.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = Spacing.xs, bottom = Spacing.xs)
                )
                suggestions.forEach { card ->
                    SuggestionCard(
                        title = card.title,
                        subtitle = card.subtitle,
                        icon = card.icon
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Bottom input area
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            RamadanColors.NavyPrimary.copy(alpha = 0.98f)
                        )
                    )
                )
                .padding(horizontal = Spacing.lg, vertical = Spacing.md)
        ) {
            // Quick prompts row (horizontal scroll)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = Spacing.sm),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                quickPrompts.forEach { prompt ->
                    QuickPromptChip(text = prompt)
                }
            }
            // Input + send
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                OutlinedTextField(
                    value = state.inputText,
                    onValueChange = viewModel::updateInput,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Share what's on your mind...",
                            style = DesignSystemTypography.body,
                            color = RamadanColors.Gold.copy(alpha = 0.4f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RamadanColors.Gold.copy(alpha = 0.4f),
                        unfocusedBorderColor = RamadanColors.BorderGold.copy(alpha = 0.15f),
                        focusedTextColor = RamadanColors.TextPrimary,
                        unfocusedTextColor = RamadanColors.TextPrimary,
                        cursorColor = RamadanColors.Gold
                    ),
                    shape = RoundedCornerShape(24.dp),
                    enabled = !state.isLoading
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (state.isLoading) RamadanColors.Gold.copy(alpha = 0.5f)
                            else RamadanColors.Gold
                        )
                        .then(
                            if (!state.isLoading) Modifier.clickable(enabled = state.inputText.isNotBlank()) {
                                viewModel.sendMessage()
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = RamadanColors.NavyPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(24.dp),
                            tint = RamadanColors.NavyPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanionAvatar() {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Box(
        modifier = Modifier.size(112.dp),
        contentAlignment = Alignment.Center
    ) {
        // Soft glow behind avatar
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RamadanColors.Gold.copy(alpha = 0.25f),
                            RamadanColors.Gold.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(96.dp)
                .rotate(rotation)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            RamadanColors.Gold,
                            RamadanColors.PurpleAccent,
                            RamadanColors.Gold
                        )
                    ),
                    shape = CircleShape
                )
                .padding(2.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            RamadanColors.NavyPrimary,
                            RamadanColors.NavyCard
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = RamadanColors.Gold
            )
        }
    }
}

@Composable
private fun ChatBubble(
    text: String,
    time: String,
    isFromUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isFromUser)
                        RamadanColors.Gold.copy(alpha = 0.15f)
                    else
                        RamadanColors.PurpleAccent.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isFromUser) RamadanColors.BorderGold.copy(alpha = 0.25f)
                    else RamadanColors.BorderGold.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(Spacing.md)
        ) {
            Column {
                Text(
                    text = text,
                    style = DesignSystemTypography.body.copy(
                        lineHeight = 19.6.sp
                    ),
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = time,
                    style = DesignSystemTypography.caption,
                    color = RamadanColors.Gold.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = Spacing.xs)
                )
            }
        }
    }
}

@Composable
private fun SuggestionCard(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit
) {
    RamadanCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = null,
        cornerRadius = 24.dp,
        contentPadding = Spacing.md
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(RamadanColors.Gold.copy(alpha = 0.15f))
                    .border(1.dp, RamadanColors.BorderGold.copy(alpha = 0.2f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = DesignSystemTypography.cardTitle,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = subtitle,
                    style = DesignSystemTypography.caption,
                    color = RamadanColors.Gold.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun QuickPromptChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                RamadanColors.PurpleAccent.copy(alpha = 0.6f),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, RamadanColors.BorderGold.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = Spacing.md, vertical = 10.dp)
    ) {
        Text(
            text = text,
            style = DesignSystemTypography.caption,
            color = RamadanColors.Gold
        )
    }
}

@Composable
private fun rememberCompanionMessages(): List<ChatMessageUi> = listOf(
    ChatMessageUi(
        text = "It's okay if today was busy. Let's rebuild your day together.",
        isFromUser = false,
        time = "2:30 PM"
    ),
    ChatMessageUi(
        text = "I missed Dhuhr prayer because of work",
        isFromUser = true,
        time = "2:28 PM"
    )
)

@Composable
private fun rememberSuggestionCards(): List<SuggestionCardUi> = listOf(
    SuggestionCardUi("5-minute ibadah plan", "Quick spiritual reset") {
        Icon(Icons.Default.Schedule, null, tint = RamadanColors.Gold, modifier = Modifier.size(24.dp))
    },
    SuggestionCardUi("Dua suggestion", "For peace of mind") {
        Icon(Icons.Default.Book, null, tint = RamadanColors.Gold, modifier = Modifier.size(24.dp))
    },
    SuggestionCardUi("Gentle motivation", "You're doing great") {
        Icon(Icons.Default.Favorite, null, tint = RamadanColors.Gold, modifier = Modifier.size(24.dp))
    }
)
