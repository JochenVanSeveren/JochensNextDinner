package be.hogent.jochensnextdinner.ui.recipes.detail

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import be.hogent.jochensnextdinner.ui.recipes.RecipeApiState
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
        is RecipeApiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is RecipeApiState.Error -> {
            Text(text = recipeApiState.message)
        }

        is RecipeApiState.Success -> {
            recipe?.let {
                Column {
                    it.image?.let { image ->
                        AsyncImage(
                            model = "${BuildConfig.CLOUDINARY_BASE_URL}${image}",
                            contentDescription = "Recipe Image ${it.title}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    it.ingredients.forEach { ingredient ->
                        Text(text = ingredient, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (it.optionalIngredients.isNotEmpty()) {
                        Text(
                            text = "Optional Ingredients",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                        it.optionalIngredients.forEach { optionalIngredient ->
                            Text(
                                text = optionalIngredient,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Text(
                        text = "Herbs",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    it.herbs.forEach { herb ->
                        Text(text = herb, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(
                        text = "Steps",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    it.steps.forEachIndexed { index, step ->
                        Text(
                            text = "${index + 1}. $step",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}