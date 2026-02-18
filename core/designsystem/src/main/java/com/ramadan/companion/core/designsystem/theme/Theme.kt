package com.ramadan.companion.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RamadanDarkColorScheme = darkColorScheme(
    primary = RamadanColors.Gold,
    onPrimary = RamadanColors.NavyPrimary,
    primaryContainer = RamadanColors.PurpleAccent,
    onPrimaryContainer = RamadanColors.TextPrimary,
    secondary = RamadanColors.GoldDark,
    onSecondary = RamadanColors.NavyPrimary,
    tertiary = RamadanColors.PurpleAccent,
    surface = RamadanColors.NavyPrimary,
    onSurface = RamadanColors.TextPrimary,
    surfaceVariant = RamadanColors.NavyCard,
    onSurfaceVariant = RamadanColors.TextSecondary,
    outline = RamadanColors.BorderGold,
    outlineVariant = RamadanColors.OverlayGold
)

@Composable
fun RamadanCompanionTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = RamadanDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = RamadanColors.NavyPrimary.toArgb()
            window.navigationBarColor = RamadanColors.NavyPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = RamadanTypography,
        content = content
    )
}
