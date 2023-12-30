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

/**
 * Composable function for the RecipeDetailScreen.
 * It displays the details of a recipe.
 *
 * @param recipeDetailViewModel The ViewModel for managing Recipe data.
 * @param recipeId The ID of the recipe to display.
 * @param showImage A flag indicating whether to show the image of the recipe.
 */
@Composable
fun RecipeDetailScreen(
    recipeDetailViewModel: RecipeDetailViewModel = viewModel(factory = RecipeDetailViewModel.Factory),
    recipeId: Long,
    showImage : Boolean = true
) {
    // Fetch the recipe details when the recipeId changes
    LaunchedEffect(recipeId) {
        recipeDetailViewModel.getRecipeDetail(recipeId)
    }

    // Observe the recipe data from the ViewModel
    val recipe by recipeDetailViewModel.recipe.collectAsState()

    // Display different UI based on the API state
    when (val recipeApiState = recipeDetailViewModel.recipeApiState) {
        // Display a CircularProgressIndicator when loading
        is RecipeApiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        // Display an error message when an error occurs
        is RecipeApiState.Error -> Text(text = recipeApiState.message)

        // Display the recipe details when the data is successfully fetched
        is RecipeApiState.Success -> recipe?.let { recipeData ->
            LazyColumn {
                // Display the recipe image if showImage is true
                if (showImage) {
                    item { RecipeImage(image = recipeData.image, title = recipeData.title) }
                }
                // Display the ingredients, optional ingredients, herbs, and steps of the recipe
                item { SectionHeader("Ingredients") }
                items(recipeData.ingredients) { ingredient -> ListItem(ingredient) }
                item { SectionHeader("Optional Ingredients") }
                items(recipeData.optionalIngredients) { optionalIngredient -> ListItem(optionalIngredient) }
                item { SectionHeader("Herbs") }
                items(recipeData.herbs) { herb -> ListItem(herb) }
                item { SectionHeader("Steps") }
                itemsIndexed(recipeData.steps) { index, step -> NumberedListItem(step, index + 1) }
            }
        }
    }
}

/**
 * Composable function for displaying the recipe image.
 *
 * @param image The URL of the image.
 * @param title The title of the recipe.
 */
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

/**
 * Composable function for displaying a section header.
 *
 * @param title The title of the section.
 */
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

/**
 * Composable function for displaying a list item.
 *
 * @param text The text of the list item.
 */
@Composable
fun ListItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

/**
 * Composable function for displaying a numbered list item.
 *
 * @param text The text of the list item.
 * @param number The number of the list item.
 */
@Composable
fun NumberedListItem(text: String, number: Int) {
    Text(
        text = "$number. $text",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}