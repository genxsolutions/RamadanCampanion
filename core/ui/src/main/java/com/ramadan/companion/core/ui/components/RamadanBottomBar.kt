package com.ramadan.companion.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
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
            Icon(
                Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f)
            )
        },
        RamadanTab(RamadanRoutes.COMPANION, "AI Companion") { selected ->
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f)
            )
        },
        RamadanTab(RamadanRoutes.QURAN, "Quran") { selected ->
            Icon(
                Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f)
            )
        },
        RamadanTab(RamadanRoutes.REFLECTION, "Reflection") { selected ->
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f)
            )
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
                if (selected) RamadanColors.Gold else RamadanColors.Gold.copy(alpha = 0.4f),
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
