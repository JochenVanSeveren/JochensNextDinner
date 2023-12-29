package be.hogent.jochensnextdinner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import be.hogent.jochensnextdinner.ui.JochensNextDinnerApp
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme
import be.hogent.jochensnextdinner.utils.JndNavigationType

/**
 * Main activity for the application.
 * This is the entry point for your app.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set the theme for the app
            JochensNextDinnerTheme(darkTheme = false) {
                // Create a new surface view
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    // Calculate the window size class
                    val windowSize = calculateWindowSizeClass(activity = this)
                    // Depending on the window size class, set the navigation type
                    when (windowSize.widthSizeClass) {
                        // If the window size class is compact, use bottom navigation
                        WindowWidthSizeClass.Compact -> {
                            JochensNextDinnerApp(navigationType = JndNavigationType.BOTTOM_NAVIGATION)
                        }
                        // If the window size class is medium, use navigation rail
                        WindowWidthSizeClass.Medium -> {
                            JochensNextDinnerApp(navigationType = JndNavigationType.NAVIGATION_RAIL)
                        }
                        // If the window size class is expanded, use permanent navigation drawer
                        WindowWidthSizeClass.Expanded -> {
                            JochensNextDinnerApp(navigationType = JndNavigationType.PERMANENT_NAVIGATION_DRAWER)
                        }
                        // For all other cases, use bottom navigation
                        else -> {
                            JochensNextDinnerApp(navigationType = JndNavigationType.BOTTOM_NAVIGATION)
                        }
                    }
                }
            }
        }
    }
}