package com.ramadan.companion.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.ramadan.companion.core.designsystem.theme.RamadanColors
import com.ramadan.companion.core.ui.navigation.RamadanRoutes

data class RamadanTab(
    val route: String,
    val label: String,
    val icon: @Composable (selected: Boolean) -> Unit
)

@Composable
fun RamadanBottomBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        RamadanTab(RamadanRoutes.TODAY, "Today") { selected ->
            AnimatedNavIcon(selected) { tint ->
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
            }
        },
        RamadanTab(RamadanRoutes.COMPANION, "AI Companion") { selected ->
            AnimatedNavIcon(selected) { tint ->
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
            }
        },
        RamadanTab(RamadanRoutes.QURAN, "Quran") { selected ->
            AnimatedNavIcon(selected) { tint ->
                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
            }
        },
        RamadanTab(RamadanRoutes.REFLECTION, "Reflection") { selected ->
            AnimatedNavIcon(selected) { tint ->
                Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
            }
        }
    )
    NavigationBar(
        modifier = modifier.background(RamadanColors.NavyPrimary),
        containerColor = RamadanColors.NavyPrimary,
        contentColor = RamadanColors.Gold
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            val contentColor by animateColorAsState(
                targetValue = if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f),
                animationSpec = tween(durationMillis = 300),
                label = "content_color"
            )
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab.route) },
                icon = { tab.icon(selected) },
                label = { Text(tab.label, color = contentColor) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = RamadanColors.Gold.copy(alpha = 0.15f),
                    selectedIconColor = RamadanColors.Gold,
                    selectedTextColor = RamadanColors.Gold,
                    unselectedIconColor = RamadanColors.Gold.copy(alpha = 0.4f),
                    unselectedTextColor = RamadanColors.Gold.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Composable
private fun AnimatedNavIcon(
    selected: Boolean,
    icon: @Composable (tint: Color) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "icon_scale"
    )
    val tint by animateColorAsState(
        targetValue = if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f),
        animationSpec = tween(durationMillis = 300),
        label = "icon_tint"
    )
    Box(
        modifier = Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        contentAlignment = Alignment.Center
    ) {
        icon(tint)
    }
}
