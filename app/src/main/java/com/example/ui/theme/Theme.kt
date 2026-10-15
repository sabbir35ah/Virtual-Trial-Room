package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CoralPrimary,
    onPrimary = TextPrimaryDark,
    primaryContainer = CoralDark,
    onPrimaryContainer = TextPrimaryDark,
    secondary = IndigoAccent,
    onSecondary = TextPrimaryDark,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = IndigoLight,
    tertiary = EmeraldFit,
    onTertiary = TextPrimaryDark,
    background = DarkBg,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkSurfaceBorder
)

private val LightColorScheme = lightColorScheme(
    primary = CoralPrimary,
    onPrimary = TextPrimaryDark,
    primaryContainer = CoralLight,
    onPrimaryContainer = TextPrimaryLight,
    secondary = IndigoAccent,
    onSecondary = TextPrimaryDark,
    secondaryContainer = LightSurfaceElevated,
    onSecondaryContainer = IndigoAccent,
    tertiary = EmeraldFit,
    onTertiary = TextPrimaryDark,
    background = LightBg,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightSurfaceBorder
)

@Composable
fun CodixelFitTheme(
    darkTheme: Boolean = true, // Default to sleek dark luxury trial room aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
