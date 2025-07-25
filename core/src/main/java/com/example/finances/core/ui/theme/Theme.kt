package com.example.finances.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = DarkestGray,
    inversePrimary = RealWhite,
    primaryContainer = LightGreen,
    onPrimaryContainer = Black,

    secondaryContainer = Red,
    onSecondaryContainer = RealWhite,

    tertiary = Orange,

    surface = White,
    onSurface = Black,
    onSurfaceVariant = DarkestGray,
    inverseSurface = LightGreen,
    surfaceContainerLow = RealWhite,
    surfaceContainer = LightestGray,
    surfaceContainerHigh = LightGray,

    outline = DarkGray,
    outlineVariant = Gray
)

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = White,
    inversePrimary = Black,
    primaryContainer = LightGreen,
    onPrimaryContainer = RealWhite,

    secondaryContainer = Red,
    onSecondaryContainer = RealWhite,

    tertiary = Orange,

    surface = DarkestGray,
    onSurface = RealWhite,
    onSurfaceVariant = White,
    inverseSurface = LightGreen,
    surfaceContainerLow = Black,
    surfaceContainer = DarkGray,
    surfaceContainerHigh = Gray,

    outline = LightestGray,
    outlineVariant = LightGray
)

@Composable
fun FinancesTheme(content: @Composable () -> Unit) {
    val themeController = LocalThemeController.current
    val primaryContainer = lightenColor(themeController.primaryColor, 0.7f)
    MaterialTheme(
        content = content,
        typography = Typography,
        colorScheme = if (themeController.darkTheme) DarkColorScheme.copy(
            primary = themeController.primaryColor,
            primaryContainer = primaryContainer
        ) else LightColorScheme.copy(
            primary = themeController.primaryColor,
            primaryContainer = primaryContainer
        )
    )

    val view = LocalView.current
    if (!view.isInEditMode) SideEffect {
        WindowCompat.getInsetsController(
            (view.context as Activity).window,
            view
        ).isAppearanceLightStatusBars = !themeController.darkTheme
    }
}

fun lightenColor(color: Color, factor: Float): Color {
    val r = (color.red + (1 - color.red) * factor).coerceIn(0f, 1f)
    val g = (color.green + (1 - color.green) * factor).coerceIn(0f, 1f)
    val b = (color.blue + (1 - color.blue) * factor).coerceIn(0f, 1f)
    return Color(r, g, b, color.alpha)
}
