package com.example.setupsheets.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Dark color scheme for dark mode using colors defined in Color.kt.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

/**
 * Light color scheme for light mode using colors defined in Color.kt.
 */
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = OnPrimaryColor,
    secondary = BlueSecondary,
    onSecondary = OnSecondaryColor,
    background = LightBackground,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor
)

/**
 * The main theme composable that applies either the light or dark color scheme
 * depending on the user's system settings or explicit preference.
 *
 * darkTheme- Determines whether dark mode should be used; defaults to system preference.
 * dynamicColor- Enables dynamic theming on Android 12+ (currently disabled by default).
 * Content- The content composable that will be themed.
 */
@Composable
fun SetupSheetsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
