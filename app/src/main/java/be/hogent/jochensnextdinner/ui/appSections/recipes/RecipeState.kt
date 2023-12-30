package be.hogent.jochensnextdinner.ui.appSections.recipes

import be.hogent.jochensnextdinner.model.Recipe

/**
 * Data class representing the state of the Recipe list.
 *
 * @property recipeList The list of Recipe items.
 */
data class RecipeListState(val recipeList: List<Recipe> = listOf())

/**
 * Sealed interface representing the state of the Recipe API.
 */
sealed interface RecipeApiState {
    /**
     * Object representing the success state of the Recipe API.
     */
    object Success : RecipeApiState

    /**
     * Data class representing the error state of the Recipe API.
     *
     * @property message The error message.
     */
    data class Error(val message: String) : RecipeApiState

    /**
     * Object representing the loading state of the Recipe API.
     */
    object Loading : RecipeApiState
}