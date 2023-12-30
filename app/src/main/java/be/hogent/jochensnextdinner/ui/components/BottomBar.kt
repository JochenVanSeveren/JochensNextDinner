package be.hogent.jochensnextdinner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import be.hogent.jochensnextdinner.utils.IconResource
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

/**
 * Composable function for creating a bottom navigation bar.
 *
 * @param navController The NavController for navigating between screens.
 * @param screens The list of screens to display in the bottom bar. By default, it includes all screens that have the inBottomBar property set to true.
 */
@Composable
fun BottomBar(
    navController: NavHostController,
    screens: List<JochensNextDinnerScreen> = JochensNextDinnerScreen.values().toList()
        .filter { it.inBottomBar }
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.tertiary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                IconButton(onClick = {
                    navController.navigate(screen.name) {
                        launchSingleTop = true
                    }
                }) {
                    when (val icon = screen.icon) {
                        is IconResource.Vector -> {
                            Icon(
                                imageVector = icon.vector,
                                contentDescription = stringResource(id = screen.label),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        is IconResource.Drawable -> {
                            Icon(
                                painter = painterResource(id = icon.resId),
                                contentDescription = stringResource(id = screen.label),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}