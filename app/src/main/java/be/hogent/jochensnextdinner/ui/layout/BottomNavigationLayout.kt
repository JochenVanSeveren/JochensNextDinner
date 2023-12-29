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
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

@Composable
fun BottomNavigationLayout(
    navController: NavHostController,
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar }
) {

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    Scaffold(
        topBar = {
            TopBar(
                navController = navController
            )
        },
        bottomBar = {
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
                BottomBar(navController, screens)
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it), color = MaterialTheme.colorScheme.background) {
            NavComponent(
                navController = navController,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}