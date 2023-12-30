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

@Composable
fun RecipesScreen(
    recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModel.Factory),
    onRecipeClick: (Long, String) -> Unit
) {
    val recipeListState by recipeViewModel.uiListState.collectAsState()
    val recipeApiState = recipeViewModel.recipeApiState
    val isRefreshing = remember { mutableStateOf(false) }

    // SCROLLLLLLLLLLLLLL
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    when (recipeApiState) {
        is RecipeApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        is RecipeApiState.Error -> {
            Column {
                Text(recipeApiState.message)
                Button(onClick = {
                    recipeViewModel.refresh()
                }) {
                    Text("Try Again")
                }
            }
        }

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
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(
                                            index = recipeListState.recipeList.indexOf(
                                                recipe
                                            )
                                        )
                                    }
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