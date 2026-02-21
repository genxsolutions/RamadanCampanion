package com.ramadan.companion.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.theme.RamadanColors

/** Dark navy base + subtle purple overlay + gold accent. For first (prayer) card. */
val RamadanCardGradientFirst = Brush.verticalGradient(
    colors = listOf(
        RamadanColors.NavyCard.copy(alpha = 0.95f),
        RamadanColors.PurpleAccent.copy(alpha = 0.6f),
        RamadanColors.PurpleAccentDark.copy(alpha = 0.5f)
    )
)

/** Slightly lighter gradient + soft overlay. For second card. */
val RamadanCardGradientSecond = Brush.verticalGradient(
    colors = listOf(
        RamadanColors.PurpleAccent.copy(alpha = 0.55f),
        RamadanColors.PurpleAccentDark.copy(alpha = 0.45f),
        RamadanColors.NavyCard.copy(alpha = 0.85f)
    )
)

@Composable
fun RamadanCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    gradient: Brush = Brush.linearGradient(
        colors = listOf(
            RamadanColors.PurpleAccent.copy(alpha = 0.8f),
            RamadanColors.PurpleAccentDark.copy(alpha = 0.6f)
        )
    ),
    borderColor: Color = RamadanColors.BorderGold.copy(alpha = 0.12f),
    cornerRadius: Dp = 24.dp,
    contentPadding: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val scale = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(150),
        label = "card_scale"
    )
    Box(
        modifier = modifier
            .scale(scale.value)
            .clip(RoundedCornerShape(cornerRadius))
            .background(gradient)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(
                if (onClick != null) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                else Modifier
            )
            .padding(contentPadding),
        content = content
    )
}
