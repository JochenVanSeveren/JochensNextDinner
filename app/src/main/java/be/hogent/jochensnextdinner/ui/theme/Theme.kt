package be.hogent.jochensnextdinner.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define the dark color scheme for the application
private val DarkColorScheme = darkColorScheme(
    primary = MyButtonColor,
    onPrimary = MyOutlining,
    tertiary = Color.DarkGray,
    background = Color.Black,
)

// Define the light color scheme for the application
private val LightColorScheme = lightColorScheme(
    primary = MyButtonColor,
    onPrimary = MyOutlining,
    tertiary = MyBackground,
    background = MyBackgroundAlternate,
)

/**
 * Composable function for applying the theme of the JochensNextDinner app.
 * It sets the color scheme based on the system theme and the dynamic color setting.
 *
 * @param darkTheme A flag indicating whether to use the dark theme. By default, it is set to the system theme.
 * @param dynamicColor A flag indicating whether to use dynamic color. By default, it is set to false. Dynamic color is available on Android 12+.
 * @param content The content to be styled with the theme.
 */
@Composable
fun JochensNextDinnerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Determine the color scheme to use
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Set the status bar color and light status bar appearance based on the color scheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // Apply the MaterialTheme with the determined color scheme and typography
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}