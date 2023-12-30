package be.hogent.jochensnextdinner.ui.appSections.recipes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import be.hogent.jochensnextdinner.JndApplication
import be.hogent.jochensnextdinner.data.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing Recipe data.
 *
 * @property recipeRepository The repository for managing Recipe data.
 */
class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    // State for the list of Recipe items
    lateinit var uiListState: StateFlow<RecipeListState>

    // State for the API status
    var recipeApiState: RecipeApiState by mutableStateOf(RecipeApiState.Loading)
        private set

    init {
        getRecipesFromRepo()
    }

    /**
     * Refreshes the list of Recipe items from the repository.
     */
    fun refresh() {
        viewModelScope.launch {
            try {
                recipeApiState = RecipeApiState.Loading
                recipeRepository.refresh()
                recipeApiState = RecipeApiState.Success
            } catch (e: Exception) {
                recipeApiState = RecipeApiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Fetches the list of Recipe items from the repository.
     */
    private fun getRecipesFromRepo() {
        try {
            recipeApiState = RecipeApiState.Loading
            uiListState = recipeRepository.getRecipes().map { RecipeListState(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000L),
                    initialValue = RecipeListState(),
                )
            recipeApiState = RecipeApiState.Success
        } catch (e: Exception) {
            recipeApiState = RecipeApiState.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        // Singleton instance of the ViewModel
        private var Instance: RecipeViewModel? = null
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
                    Instance = RecipeViewModel(recipeRepository = recipeRepository)
                }
                // Return the instance of the ViewModel
                Instance!!
            }
        }
    }
}