package be.hogent.jochensnextdinner.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import be.hogent.jochensnextdinner.ui.components.BottomBar
import be.hogent.jochensnextdinner.ui.components.TopBar
import be.hogent.jochensnextdinner.ui.navigation.NavComponent
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

/**
 * Composable function for creating a layout with a bottom navigation bar.
 *
 * @param navController The NavController for navigating between screens.
 * @param screens The list of screens to display in the bottom bar. By default, it includes all screens that have the inBottomBar property set to true.
 */
@Composable
fun BottomNavigationLayout(
    navController: NavHostController,
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar }
) {

    // Observe the current destination from the NavController
    val currentDestination by navController.currentBackStackEntryAsState()
    // Determine the current route based on the current destination
    val currentRoute = currentDestination?.destination?.route

    // Create a Scaffold with a top bar and a bottom bar
    Scaffold(
        topBar = {
            // Create a TopBar with the NavController
            TopBar(
                navController = navController
            )
        },
        bottomBar = {
            // Create an AnimatedVisibility that shows or hides the bottom bar based on whether the current route is the start screen
            AnimatedVisibility(
                visible = currentRoute == JochensNextDinnerScreen.Start.name,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 500)
                )
            ) {
                // Create a BottomBar with the NavController and the list of screens
                BottomBar(navController, screens)
            }
        }
    ) {
        // Create a Surface with padding and a background color
        Surface(modifier = Modifier.padding(it), color = MaterialTheme.colorScheme.background) {
            // Create a NavComponent with the NavController, padding, and the navigation type set to BOTTOM_NAVIGATION
            NavComponent(
                navController = navController,
                modifier = Modifier.padding(16.dp),
                navigationType = JndNavigationType.BOTTOM_NAVIGATION
            )
        }
    }
}