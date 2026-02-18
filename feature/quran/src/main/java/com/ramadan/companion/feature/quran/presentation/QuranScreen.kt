package com.ramadan.companion.feature.quran.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.components.RamadanCard
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.designsystem.theme.RamadanTypography

@Composable
fun QuranScreen(modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header card - Suggested Surah
            RamadanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = null,
                cornerRadius = 28.dp,
                contentPadding = 20.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Suggested for you",
                                style = RamadanTypography.labelSmall,
                                color = RamadanColors.Gold.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Surah Al-Kahf",
                                style = RamadanTypography.headlineMedium,
                                color = RamadanColors.TextPrimary
                            )
                            Text(
                                text = "The Cave",
                                style = RamadanTypography.bodySmall,
                                color = RamadanColors.TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(RamadanColors.Gold.copy(alpha = 0.15f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "~10 mins",
                                style = RamadanTypography.labelSmall,
                                color = RamadanColors.Gold
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = RamadanColors.Gold
                        )
                        Text(
                            text = "Perfect for your Friday routine",
                            style = RamadanTypography.labelSmall,
                            color = RamadanColors.Gold.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Verse number badge
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(RamadanColors.Gold.copy(alpha = 0.1f))
                        .border(1.dp, RamadanColors.BorderGold.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Ayah 1",
                        style = RamadanTypography.labelSmall,
                        color = RamadanColors.Gold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Arabic text
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    style = RamadanTypography.headlineMedium,
                    color = RamadanColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ",
                    style = RamadanTypography.titleLarge,
                    color = RamadanColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا",
                    style = RamadanTypography.titleLarge,
                    color = RamadanColors.TextPrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Translation card
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
                Column {
                    Text(
                        text = "TRANSLATION",
                        style = RamadanTypography.labelSmall,
                        color = RamadanColors.Gold.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "[All] praise is [due] to Allah, who has sent down upon His Servant the Book and has not made therein any deviance.",
                        style = RamadanTypography.bodySmall,
                        color = RamadanColors.TextPrimary.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Next verse preview
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(RamadanColors.Gold.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Ayah 2",
                        style = RamadanTypography.labelSmall,
                        color = RamadanColors.Gold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ",
                    style = RamadanTypography.titleMedium,
                    color = RamadanColors.TextPrimary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(140.dp))
        }

        // Bottom controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            RamadanColors.NavyPrimary.copy(alpha = 0.98f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RamadanColors.PurpleAccent.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous", tint = RamadanColors.Gold)
                }
                IconButton(
                    onClick = { isBookmarked = !isBookmarked },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (isBookmarked) RamadanColors.Gold.copy(alpha = 0.2f)
                            else RamadanColors.PurpleAccent.copy(alpha = 0.6f)
                        )
                ) {
                    Icon(
                        if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = RamadanColors.Gold
                    )
                }
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(RamadanColors.Gold)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = RamadanColors.NavyPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RamadanColors.PurpleAccent.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = RamadanColors.Gold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(RamadanColors.PurpleAccent.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = RamadanColors.Gold)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "AI suggests reading 5 more ayahs today",
                    style = RamadanTypography.labelSmall,
                    color = RamadanColors.Gold
                )
            }
        }
    }
}
