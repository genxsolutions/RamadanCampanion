package com.ramadan.companion.feature.companion.presentation

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.components.RamadanCard
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.RamadanTypography

private data class ChatMessageUi(
    val text: String,
    val isFromUser: Boolean,
    val time: String
)

private data class SuggestionCardUi(
    val title: String,
    val subtitle: String,
    val icon: @Composable () -> Unit
)

@Composable
fun CompanionScreen(
    onUpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val messages = rememberCompanionMessages()
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
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header with avatar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompanionAvatar()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your AI Companion",
                    style = RamadanTypography.headlineMedium,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = "Here to guide your day",
                    style = RamadanTypography.labelSmall,
                    color = RamadanColors.Gold.copy(alpha = 0.7f)
                )
            }

            // Chat messages
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                messages.forEach { msg ->
                    ChatBubble(
                        text = msg.text,
                        time = msg.time,
                        isFromUser = msg.isFromUser
                    )
                }

                // Suggestion cards
                Text(
                    text = "Suggestions for you:",
                    style = RamadanTypography.labelSmall,
                    color = RamadanColors.Gold.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                suggestions.forEach { card ->
                    SuggestionCard(
                        title = card.title,
                        subtitle = card.subtitle,
                        icon = card.icon
                    )
                    Spacer(modifier = Modifier.height(12.dp))
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Quick prompts row (horizontal scroll)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickPrompts.forEach { prompt ->
                    QuickPromptChip(text = prompt)
                }
            }
            // Input + send
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Share what's on your mind...",
                            style = RamadanTypography.bodyMedium,
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
                    shape = RoundedCornerShape(24.dp)
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RamadanColors.Gold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = RamadanColors.NavyPrimary
                    )
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
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
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
                .padding(horizontal = if (isFromUser) 0.dp else 0.dp)
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
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = text,
                    style = RamadanTypography.bodyMedium,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = time,
                    style = RamadanTypography.labelSmall,
                    color = RamadanColors.Gold.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 8.dp)
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
        contentPadding = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
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
                    style = RamadanTypography.bodyMedium,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = subtitle,
                    style = RamadanTypography.labelSmall,
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
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            style = RamadanTypography.labelSmall,
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
