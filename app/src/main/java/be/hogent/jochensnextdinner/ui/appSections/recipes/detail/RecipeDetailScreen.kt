package be.hogent.jochensnextdinner.ui.appSections.recipes.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.ui.appSections.recipes.RecipeApiState
import coil.compose.AsyncImage

@Composable
fun RecipeDetailScreen(
    recipeDetailViewModel: RecipeDetailViewModel = viewModel(factory = RecipeDetailViewModel.Factory),
    recipeId: Long
) {
    LaunchedEffect(recipeId) {
        recipeDetailViewModel.getRecipeDetail(recipeId)
    }

    val recipe by recipeDetailViewModel.recipe.collectAsState()

    when (val recipeApiState = recipeDetailViewModel.recipeApiState) {
        is RecipeApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        is RecipeApiState.Error -> Text(text = recipeApiState.message)

        is RecipeApiState.Success -> recipe?.let { recipeData ->
            LazyColumn {
                item { RecipeImage(image = recipeData.image, title = recipeData.title) }
                item { SectionHeader("Ingredients") }
                items(recipeData.ingredients) { ingredient -> ListItem(ingredient) }

                item { SectionHeader("Optional Ingredients") }
                items(recipeData.optionalIngredients) { optionalIngredient ->
                    ListItem(
                        optionalIngredient
                    )
                }

                item { SectionHeader("Herbs") }
                items(recipeData.herbs) { herb -> ListItem(herb) }

                item { SectionHeader("Steps") }
                itemsIndexed(recipeData.steps) { index, step -> NumberedListItem(step, index + 1) }
            }
        }
    }
}

@Composable
fun RecipeImage(image: String?, title: String) {
    image?.let {
        AsyncImage(
            model = "${BuildConfig.CLOUDINARY_BASE_URL}$it",
            contentDescription = "Recipe Image $title",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ListItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

@Composable
fun NumberedListItem(text: String, number: Int) {
    Text(
        text = "$number. $text",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}
