package com.example.financial_app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Green,
    onPrimary = DarkestGray,
    inversePrimary = RealWhite,
    primaryContainer = LightGreen,
    onPrimaryContainer = Black,

    secondaryContainer = Red,
    onSecondaryContainer = RealWhite,

    surface = White,
    onSurface = Black,
    onSurfaceVariant = DarkestGray,
    inverseSurface = LightGreen,
    surfaceContainerLowest = RealWhite,
    surfaceContainerLow = LightestGray,
    surfaceContainer = LightGray,

    outline = DarkGray,
    outlineVariant = Gray
)

private val DarkColorScheme = LightColorScheme
// TODO: Заменить на инициализацию через darkColorScheme, когда будет задание сделать темную тему

@Composable
fun FinancialAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
