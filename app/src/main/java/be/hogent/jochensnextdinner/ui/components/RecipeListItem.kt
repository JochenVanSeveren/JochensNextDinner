package be.hogent.jochensnextdinner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.model.Recipe
import coil.compose.AsyncImage

@Composable
fun RecipeListItem(
    recipe: Recipe,
    onRecipeClick: (Recipe) -> Unit
) {
    Column {
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(8.dp))
        recipe.image?.let {
            AsyncImage(
                model = "${BuildConfig.CLOUDINARY_BASE_URL}${recipe.image}",
                contentDescription = "Recipe Image ${recipe.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onRecipeClick(recipe) }
            )
        }
    }
}
