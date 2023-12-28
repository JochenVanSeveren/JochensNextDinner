package be.hogent.jochensnextdinner.ui.recipes

import be.hogent.jochensnextdinner.model.Recipe

data class RecipeListState(val recipeList: List<Recipe> = listOf())

sealed interface RecipeApiState {
    object Success : RecipeApiState
    data class Error(val message: String) : RecipeApiState
    object Loading : RecipeApiState
}