package be.hogent.jochensnextdinner.ui.recipes

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
import be.hogent.jochensnextdinner.model.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    lateinit var uiListState: StateFlow<RecipeListState>

    var recipeApiState: RecipeApiState by mutableStateOf(RecipeApiState.Loading)
        private set

    init {
        getRecipesFromRepo()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                recipeApiState = RecipeApiState.Loading
                recipeRepository.refresh()
                recipeApiState = RecipeApiState.Success
            } catch (e: Exception) {
                recipeApiState = RecipeApiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

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
        } catch (e: IOException) {
            recipeApiState = RecipeApiState.Error(e.message ?: "Unknown error")
        }
    }

    companion object {
        private var Instance: RecipeViewModel? = null
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                if (Instance == null) {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as JndApplication)
                    val recipeRepository = application.appContainer.recipeRepository
                    Instance = RecipeViewModel(recipeRepository = recipeRepository)
                }
                Instance!!
            }
        }
    }
}