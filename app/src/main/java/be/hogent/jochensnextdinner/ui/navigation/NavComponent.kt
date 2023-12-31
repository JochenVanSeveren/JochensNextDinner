package be.hogent.jochensnextdinner.ui.navigation

import CantEatScreen
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import be.hogent.jochensnextdinner.ui.StartScreen
import be.hogent.jochensnextdinner.ui.appSections.likes.LikesScreen
import be.hogent.jochensnextdinner.ui.appSections.recipes.RecipesScreen
import be.hogent.jochensnextdinner.ui.appSections.recipes.detail.RecipeDetailScreen
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

/**
 * Composable function for creating a navigation component.
 * It sets up the navigation routes and their corresponding screens.
 *
 * @param navController The NavController for navigating between screens.
 * @param navigationType The type of navigation to use (e.g., bottom navigation, drawer navigation).
 * @param modifier The modifier to apply to the NavHost.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NavComponent(
    navController: NavHostController,
    navigationType: JndNavigationType,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = JochensNextDinnerScreen.Start.name,
        modifier = modifier,
    ) {
        composable(route = JochensNextDinnerScreen.Start.name) {
            StartScreen(onScreenClick = { screen ->
                navController.navigate(screen.name) {
                    launchSingleTop = true
                }
            }, navigationType = navigationType)
        }
        composable(route = JochensNextDinnerScreen.CantEatScreen.name) {
            CantEatScreen()
        }
        composable(route = JochensNextDinnerScreen.LikeScreen.name) {
            LikesScreen()
        }
        composable(route = JochensNextDinnerScreen.RecipeScreen.name) {

            var selectedItem: Long? by rememberSaveable { mutableStateOf(null) }
            if (navigationType == JndNavigationType.PERMANENT_NAVIGATION_DRAWER) {
                ListDetailPaneScaffold(
                    listPane = {
                        RecipesScreen(onRecipeClick = { localId, _ ->
                            selectedItem = localId
                        })
                    },
                    detailPane = {
                        selectedItem?.let { RecipeDetailScreen(recipeId = it, showImage = false) }
                    },
                )
            } else {
                RecipesScreen(onRecipeClick = { localId, title ->
                    navController.navigate("RecipeDetailScreen/${localId}?title=${title}") {
                        launchSingleTop = true
                    }
                })
            }
        }
        composable("RecipeDetailScreen/{recipeId}?title={title}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull()
            if (recipeId != null) {
                RecipeDetailScreen(recipeId = recipeId)
            }
        }
    }
}