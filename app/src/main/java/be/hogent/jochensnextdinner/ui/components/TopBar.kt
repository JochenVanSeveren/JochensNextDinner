package be.hogent.jochensnextdinner.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

/**
 * Composable function for creating a top bar.
 *
 * @param navController The NavController for navigating between screens.
 * @param showGoBackButton A flag indicating whether to show the go back button. By default, it is set to true.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    showGoBackButton: Boolean = true
) {
    // Determine if the user can go back based on the previous back stack entry
    val canGoBack = navController.previousBackStackEntry != null

    // Observe the current destination from the NavController
    val currentDestination by navController.currentBackStackEntryAsState()
    // Determine the title of the top bar based on the current destination
    val title = when (currentDestination?.destination?.route) {
        JochensNextDinnerScreen.CantEatScreen.name -> stringResource(id = R.string.cant_eat_screen)
        JochensNextDinnerScreen.LikeScreen.name -> stringResource(id = R.string.like_screen)
        JochensNextDinnerScreen.RecipeScreen.name -> stringResource(id = R.string.recipe_screen)
        "RecipeDetailScreen/{recipeId}?title={title}" -> currentDestination?.arguments?.getString("title")
            ?: stringResource(id = R.string.app_name)

        else -> stringResource(id = R.string.app_name)
    }

    // Create a CenterAlignedTopAppBar with the determined title and a navigation icon if the user can go back
    CenterAlignedTopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (canGoBack && showGoBackButton) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigate_up),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}