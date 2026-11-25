package com.jukhg10.hanzimemo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.jukhg10.hanzimemo.data.preferences.AppThemeMode

val LocalIsDarkTheme = staticCompositionLocalOf { false }

@Composable
@ReadOnlyComposable
fun currentIsDark(): Boolean = LocalIsDarkTheme.current

@Composable
fun rememberToneColor(tone: Int): androidx.compose.ui.graphics.Color {
    return getPinyinToneColor(tone, LocalIsDarkTheme.current)
}

private val LightColorScheme = lightColorScheme(
    primary = ImperialJadePrimaryLight,
    onPrimary = ImperialJadeOnPrimaryLight,
    primaryContainer = ImperialJadeContainerLight,
    onPrimaryContainer = ImperialJadeOnContainerLight,
    
    secondary = CinnabarSecondaryLight,
    onSecondary = CinnabarOnSecondaryLight,
    secondaryContainer = CinnabarContainerLight,
    onSecondaryContainer = CinnabarOnContainerLight,
    
    tertiary = AmberTertiaryLight,
    onTertiary = AmberOnTertiaryLight,
    tertiaryContainer = AmberContainerLight,
    onTertiaryContainer = AmberOnContainerLight,
    
    background = RicePaperBackgroundLight,
    onBackground = RicePaperOnBackgroundLight,
    
    surface = RicePaperSurfaceLight,
    onSurface = RicePaperOnSurfaceLight,
    surfaceVariant = RicePaperSurfaceVariantLight,
    onSurfaceVariant = RicePaperOnSurfaceVariantLight,
    
    outline = RicePaperOutlineLight,
    outlineVariant = RicePaperOutlineVariantLight,
    
    error = ErrorCrimson,
    onError = RicePaperSurfaceLight
)

private val DarkColorScheme = darkColorScheme(
    primary = ImperialJadePrimaryDark,
    onPrimary = ImperialJadeOnPrimaryDark,
    primaryContainer = ImperialJadeContainerDark,
    onPrimaryContainer = ImperialJadeOnContainerDark,
    
    secondary = CinnabarSecondaryDark,
    onSecondary = CinnabarOnSecondaryDark,
    secondaryContainer = CinnabarContainerDark,
    onSecondaryContainer = CinnabarOnContainerDark,
    
    tertiary = AmberTertiaryDark,
    onTertiary = AmberOnTertiaryDark,
    tertiaryContainer = AmberContainerDark,
    onTertiaryContainer = AmberOnContainerDark,
    
    background = DarkInkBackgroundDark,
    onBackground = DarkInkOnBackgroundDark,
    
    surface = DarkInkSurfaceDark,
    onSurface = DarkInkOnSurfaceDark,
    surfaceVariant = DarkInkSurfaceVariantDark,
    onSurfaceVariant = DarkInkOnSurfaceVariantDark,
    
    outline = DarkInkOutlineDark,
    outlineVariant = DarkInkOutlineVariantDark,
    
    error = ErrorCrimson,
    onError = DarkInkBackgroundDark
)

@Composable
fun HanziMemoTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    darkTheme: Boolean = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    },
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
