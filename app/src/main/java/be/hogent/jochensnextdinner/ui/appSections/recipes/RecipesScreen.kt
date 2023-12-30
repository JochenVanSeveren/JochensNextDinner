package be.hogent.jochensnextdinner.ui.appSections.recipes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.R
import be.hogent.jochensnextdinner.ui.components.RecipeListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

/**
 * Composable function for the RecipesScreen.
 * It displays a list of recipes and handles user interactions such as refreshing the list and clicking on a recipe.
 *
 * @param recipeViewModel The ViewModel for managing Recipe data.
 * @param onRecipeClick The function to be invoked when a recipe is clicked.
 */
@Composable
fun RecipesScreen(
    recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModel.Factory),
    onRecipeClick: (Long, String) -> Unit
) {
    // State for the list of Recipe items
    val recipeListState by recipeViewModel.uiListState.collectAsState()
    // State for the API status
    val recipeApiState = recipeViewModel.recipeApiState
    // State for the refreshing status
    val isRefreshing = remember { mutableStateOf(false) }

    // Remember the state of the list and the coroutine scope for scrolling
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Display different UI based on the API status
    when (recipeApiState) {
        // Display a CircularProgressIndicator when loading
        is RecipeApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        // Display an error message and a retry button when an error occurs
        is RecipeApiState.Error -> {
            Column {
                Text(recipeApiState.message)
                Button(onClick = {
                    recipeViewModel.refresh()
                }) {
                    Text(stringResource(id =R.string.try_again))
                }
            }
        }

        // Display the recipe details when the data is successfully fetched
        is RecipeApiState.Success -> {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
                onRefresh = {
                    isRefreshing.value = true
                    recipeViewModel.refresh()
                    isRefreshing.value = false
                }
            ) {
                if (recipeListState.recipeList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.no_recipes_found),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(state = listState) {
                        items(recipeListState.recipeList) { recipe ->
                            RecipeListItem(
                                recipe = recipe,
                                onRecipeClick = {
                                    // Scroll to the clicked recipe
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(
                                            index = recipeListState.recipeList.indexOf(
                                                recipe
                                            )
                                        )
                                    }
                                    // Invoke the onRecipeClick function
                                    onRecipeClick(it.localId, it.title)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}