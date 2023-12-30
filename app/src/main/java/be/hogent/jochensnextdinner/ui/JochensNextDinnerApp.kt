package be.hogent.jochensnextdinner.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import be.hogent.jochensnextdinner.ui.layout.BottomNavigationLayout
import be.hogent.jochensnextdinner.ui.layout.DrawerNavigationLayout
import be.hogent.jochensnextdinner.ui.theme.JochensNextDinnerTheme
import be.hogent.jochensnextdinner.utils.JndNavigationType

/**
 * Composable function for the JochensNextDinnerApp.
 * It sets up the navigation for the app based on the navigation type.
 *
 * @param navController The NavController for navigating between screens. By default, it uses rememberNavController to remember the NavController across compositions.
 * @param navigationType The type of navigation to use (e.g., bottom navigation, drawer navigation).
 */
@Composable
fun JochensNextDinnerApp(
    navController: NavHostController = rememberNavController(), navigationType: JndNavigationType,
) {
    when (navigationType) {
        // Use bottom navigation when the navigation type is BOTTOM_NAVIGATION
        JndNavigationType.BOTTOM_NAVIGATION -> {
            BottomNavigationLayout(navController)
        }

        // Use drawer navigation when the navigation type is PERMANENT_NAVIGATION_DRAWER
        JndNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            DrawerNavigationLayout(navController)
        }
    }
}

/**
 * Preview function for the JochensNextDinnerApp with bottom navigation.
 * It wraps the JochensNextDinnerApp in the JochensNextDinnerTheme and sets the navigation type to BOTTOM_NAVIGATION.
 */
@Preview(showBackground = true)
@Composable
fun JochensNextDinnerAppPreviewBOTTOM_NAVIGATION() {
    JochensNextDinnerTheme {
        JochensNextDinnerApp(navigationType = JndNavigationType.BOTTOM_NAVIGATION)
    }
}

/**
 * Preview function for the JochensNextDinnerApp with drawer navigation.
 * It wraps the JochensNextDinnerApp in the JochensNextDinnerTheme and sets the navigation type to PERMANENT_NAVIGATION_DRAWER.
 */
@Preview(showBackground = true, widthDp = 900)
@Composable
fun JochensNextDinnerAppPreviewPERMANENT_NAVIGATION_DRAWER() {
    JochensNextDinnerTheme() {
        JochensNextDinnerApp(navigationType = JndNavigationType.PERMANENT_NAVIGATION_DRAWER)
    }
}