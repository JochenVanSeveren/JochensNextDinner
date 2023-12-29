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

class RecipeDetailViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> get() = _recipe

    var recipeApiState: RecipeApiState by mutableStateOf(RecipeApiState.Loading)
        private set

    suspend fun getRecipeDetail(recipeId: Long) {  
        try {
            recipeApiState = RecipeApiState.Loading
            val recipe = recipeRepository.getRecipe(recipeId).first()
            _recipe.value = recipe
            recipeApiState = RecipeApiState.Success
        } catch (e: Exception) {
            recipeApiState = RecipeApiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    companion object {
        private var Instance: RecipeDetailViewModel? = null
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JndApplication)
                    val recipeRepository = application.appContainer.recipeRepository
                    Instance = RecipeDetailViewModel(recipeRepository = recipeRepository)
                }
                Instance!!
            }
        }
    }
}