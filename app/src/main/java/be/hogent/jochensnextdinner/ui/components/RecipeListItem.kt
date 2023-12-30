package be.hogent.jochensnextdinner.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.model.Recipe
import coil.compose.AsyncImage

/**
 * Composable function for displaying a list item for a Recipe object.
 * It provides an option for clicking on the Recipe object.
 *
 * @param recipe The Recipe object to display.
 * @param onRecipeClick The function to be invoked when the Recipe object is clicked.
 */
@Composable
fun RecipeListItem(
    recipe: Recipe,
    onRecipeClick: (Recipe) -> Unit
) {
    Column {
        // Display the title of the Recipe object in bold text
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(8.dp))
        // Display the image of the Recipe object if it exists
        recipe.image?.let {
            AsyncImage(
                // The URL of the image is constructed using the base URL from the BuildConfig and the image path from the Recipe object
                model = "${BuildConfig.CLOUDINARY_BASE_URL}${recipe.image}",
                // The content description is used for accessibility purposes. It describes the image for users who might not be able to see it.
                contentDescription = "Recipe Image ${recipe.title}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    // Set the maximum width of the image to 500.dp
                    .widthIn(max = 500.dp)
                    // Center the image horizontally
                    .align(Alignment.CenterHorizontally)
                    // Clip the image to a shape with rounded corners
                    .clip(RoundedCornerShape(8.dp))
                    // Set the click action for the image. When the image is clicked, the onRecipeClick function is invoked with the Recipe object.
                    .clickable { onRecipeClick(recipe) }
            )
        }
    }
}