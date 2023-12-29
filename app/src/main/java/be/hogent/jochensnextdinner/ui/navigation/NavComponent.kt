package be.hogent.jochensnextdinner.ui.navigation

import CantEatScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import be.hogent.jochensnextdinner.ui.appSections.canteats.likes.LikesScreen
import be.hogent.jochensnextdinner.ui.appSections.recipes.RecipesScreen
import be.hogent.jochensnextdinner.ui.appSections.recipes.detail.RecipeDetailScreen
import be.hogent.jochensnextdinner.ui.StartScreen
import be.hogent.jochensnextdinner.utils.JochensNextDinnerScreen

@Composable
fun NavComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = JochensNextDinnerScreen.Start.name,
        modifier = modifier,
    ) {
        composable(route = JochensNextDinnerScreen.Start.name) {
            StartScreen(
                onCantEatClick = { navController.navigate(JochensNextDinnerScreen.CantEatScreen.name) },
                onLikeClick = { navController.navigate(JochensNextDinnerScreen.LikeScreen.name) },
                onRecipeClick = { navController.navigate(JochensNextDinnerScreen.RecipeScreen.name) }
            )
        }
        composable(route = JochensNextDinnerScreen.CantEatScreen.name) {
            CantEatScreen()
        }
        composable(route = JochensNextDinnerScreen.LikeScreen.name) {
            LikesScreen()
        }
        composable(route = JochensNextDinnerScreen.RecipeScreen.name) {
            RecipesScreen(onRecipeClick = { localId, title ->
                navController.navigate("RecipeDetailScreen/${localId}?title=${title}")
            })
        }
        composable("RecipeDetailScreen/{recipeId}?title={title}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull()
            if (recipeId != null) {
                RecipeDetailScreen(recipeId = recipeId)
            }
        }
    }
}