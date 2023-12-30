package be.hogent.jochensnextdinner.ui.appSections.recipes.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.hogent.jochensnextdinner.JndApplication
import be.hogent.jochensnextdinner.data.RecipeRepository
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.ui.appSections.recipes.RecipeApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first

/**
 * ViewModel for managing Recipe data.
 *
 * @property recipeRepository The repository for managing Recipe data.
 */
class RecipeDetailViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    // Mutable state flow for the Recipe object
    private val _recipe = MutableStateFlow<Recipe?>(null)
    // Public state flow for the Recipe object
    val recipe: StateFlow<Recipe?> get() = _recipe

    // Mutable state for the API status
    var recipeApiState: RecipeApiState by mutableStateOf(RecipeApiState.Loading)
        private set

    /**
     * Fetches the details of a recipe by its ID.
     *
     * @param recipeId The ID of the recipe to fetch.
     */
    suspend fun getRecipeDetail(recipeId: Long) {
        try {
            // Set the API status to loading
            recipeApiState = RecipeApiState.Loading
            // Fetch the recipe from the repository
            val recipe = recipeRepository.getRecipe(recipeId).first()
            // Update the recipe state
            _recipe.value = recipe
            // Set the API status to success
            recipeApiState = RecipeApiState.Success
        } catch (e: Exception) {
            // Set the API status to error with the exception message
            recipeApiState = RecipeApiState.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        // Singleton instance of the ViewModel
        private var Instance: RecipeDetailViewModel? = null
        // Factory for creating the ViewModel
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    // Get the application instance
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JndApplication)
                    // Get the recipe repository from the application container
                    val recipeRepository = application.appContainer.recipeRepository
                    // Create a new instance of the ViewModel
                    Instance = RecipeDetailViewModel(recipeRepository = recipeRepository)
                }
                // Return the instance of the ViewModel
                Instance!!
            }
        }
    }
}