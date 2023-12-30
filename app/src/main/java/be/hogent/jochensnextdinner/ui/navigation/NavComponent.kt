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
import be.hogent.jochensnextdinner.ui.appSections.canteats.likes.LikesScreen
import be.hogent.jochensnextdinner.ui.appSections.recipes.RecipesScreen
import be.hogent.jochensnextdinner.ui.appSections.recipes.detail.RecipeDetailScreen
import be.hogent.jochensnextdinner.utils.JndNavigationType
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

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
            StartScreen(
                onCantEatClick = {
                    navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) {
                        launchSingleTop = true
                    }
                },
                onLikeClick = {
                    navController.navigate(JochensNextDinnerScreen.LikeScreen.name) {
                        launchSingleTop = true
                    }
                },
                onRecipeClick = {
                    navController.navigate(JochensNextDinnerScreen.RecipeScreen.name) {
                        launchSingleTop = true
                    }
                }
            )
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