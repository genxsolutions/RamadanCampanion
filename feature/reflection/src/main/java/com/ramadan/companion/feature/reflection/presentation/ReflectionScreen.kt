package com.ramadan.companion.feature.reflection.presentation

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.components.RamadanCard
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.RamadanTypography

private data class InsightUi(
    val text: String,
    val subtext: String,
    val icon: @Composable () -> Unit
)

private data class PastReflectionUi(
    val day: String,
    val text: String
)

@Composable
fun ReflectionScreen(modifier: Modifier = Modifier) {
    var gratitudeText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val insights = rememberInsights()
    val pastReflections = rememberPastReflections()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Header
        Text(
            text = "Reflection",
            style = RamadanTypography.headlineMedium,
            color = RamadanColors.TextPrimary
        )
        Text(
            text = "Nurture your spiritual growth",
            style = RamadanTypography.bodySmall,
            color = RamadanColors.Gold.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Reflection card
        RamadanCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = null,
            cornerRadius = 28.dp,
            contentPadding = 24.dp
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(RamadanColors.Gold.copy(alpha = 0.15f))
                            .border(1.dp, RamadanColors.BorderGold.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = RamadanColors.Gold)
                    }
                    Text(
                        text = "Today's Reflection",
                        style = RamadanTypography.titleMedium,
                        color = RamadanColors.TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "What are you grateful for today?",
                    style = RamadanTypography.bodyMedium,
                    color = RamadanColors.Gold
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = gratitudeText,
                    onValueChange = { gratitudeText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(
                            "Write your thoughts here...",
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
                    shape = RoundedCornerShape(20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RamadanColors.Gold, contentColor = RamadanColors.NavyPrimary),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Save Reflection", style = RamadanTypography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weekly Insights
        Text(
            text = "Weekly Insights",
            style = RamadanTypography.titleMedium,
            color = RamadanColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        insights.forEach { insight ->
            RamadanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = null,
                gradient = androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(
                        RamadanColors.OverlayLight,
                        RamadanColors.OverlayLight
                    )
                ),
                borderColor = RamadanColors.BorderGold.copy(alpha = 0.1f),
                cornerRadius = 24.dp,
                contentPadding = 20.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        insight.icon()
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = insight.text,
                            style = RamadanTypography.bodySmall,
                            color = RamadanColors.TextPrimary
                        )
                        Text(
                            text = insight.subtext,
                            style = RamadanTypography.labelSmall,
                            color = RamadanColors.TextSecondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Your Growth card
        RamadanCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = null,
            cornerRadius = 28.dp,
            contentPadding = 32.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Growth",
                    style = RamadanTypography.titleMedium,
                    color = RamadanColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Day 14 of nurturing",
                    style = RamadanTypography.bodyMedium,
                    color = RamadanColors.TextPrimary
                )
                Text(
                    text = "Your spiritual practice is blossoming beautifully",
                    style = RamadanTypography.bodySmall,
                    color = RamadanColors.Gold.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(14) { i ->
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    RamadanColors.Gold.copy(
                                        alpha = 1f - (i * 0.04f).coerceAtLeast(0f)
                                    )
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Past Reflections
        Text(
            text = "Past Reflections",
            style = RamadanTypography.titleMedium,
            color = RamadanColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))
        pastReflections.forEach { item ->
            RamadanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = null,
                gradient = androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(
                        RamadanColors.OverlayLight,
                        RamadanColors.OverlayLight
                    )
                ),
                borderColor = RamadanColors.BorderGold.copy(alpha = 0.1f),
                cornerRadius = 20.dp,
                contentPadding = 16.dp
            ) {
                Column {
                    Text(
                        text = item.day,
                        style = RamadanTypography.labelSmall,
                        color = RamadanColors.Gold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.text,
                        style = RamadanTypography.bodySmall,
                        color = RamadanColors.TextSecondary,
                        maxLines = 2
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun rememberInsights(): List<InsightUi> = listOf(
    InsightUi(
        text = "This week you were most consistent after Maghrib",
        subtext = "Your evening ibadah routine is becoming a beautiful habit 🌙",
        icon = { Icon(Icons.Default.AutoAwesome, null, tint = RamadanColors.Gold, modifier = Modifier.size(20.dp)) }
    ),
    InsightUi(
        text = "You've completed 42 dhikr sessions this week",
        subtext = "That's 28% more than last week ✨",
        icon = { Icon(Icons.Default.TrendingUp, null, tint = RamadanColors.Gold, modifier = Modifier.size(20.dp)) }
    )
)

@Composable
private fun rememberPastReflections(): List<PastReflectionUi> = listOf(
    PastReflectionUi(
        day = "Yesterday • Day 13",
        text = "I'm grateful for the quiet moments of prayer that brought me peace during a busy day..."
    ),
    PastReflectionUi(
        day = "2 days ago • Day 12",
        text = "Thankful for my family's health and the blessing of sharing iftar together..."
    )
)
