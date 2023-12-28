package be.hogent.jochensnextdinner.ui.recipes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.ui.components.RecipeListItem

@Composable
fun RecipesScreen(    recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModel.Factory),
) {
    // Display the list using a LazyColumn
//    Box(modifier = Modifier.fillMaxSize()) {
//        LazyColumn {
//            val dummyList = List(100) { "Item $it" }
//            items(dummyList) { item ->
//                Text(text = item)
//            }
//        }
//    }
    val recipeListState by recipeViewModel.uiListState.collectAsState()

    LazyColumn() {
        items(recipeListState.recipeList) { recipe ->
            RecipeListItem(
                recipe = recipe,
            )
        }
    }

}

//AsyncImage(
//model = ImageRequest.Builder(context = LocalContext.current).data(photo.imgSrc)
//.crossfade(true).build(),
//error = painterResource(R.drawable.ic_broken_image),
//placeholder = painterResource(R.drawable.loading_img),
//contentDescription = stringResource(R.string.mars_photo),
//contentScale = ContentScale.Crop,
//modifier = Modifier.fillMaxWidth()
//)