package be.hogent.jochensnextdinner.network

import be.hogent.jochensnextdinner.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

/**
 * Data class representing a Recipe item from the API.
 *
 * @property id The server ID of the Recipe item.
 * @property slug The slug of the Recipe item.
 * @property title The title of the Recipe item.
 * @property ingredients The list of ingredients for the Recipe item.
 * @property optionalIngredients The list of optional ingredients for the Recipe item.
 * @property herbs The list of herbs for the Recipe item.
 * @property steps The list of steps for the Recipe item.
 * @property image The image URL of the Recipe item.
 * @property authorId The ID of the author of the Recipe item.
 */
@Serializable
data class ApiRecipe(
    val id: String? = null,
    val slug: String,
    val title: String,
    val ingredients: List<String>,
    val optionalIngredients: List<String>,
    val herbs: List<String>,
    val steps: List<String>,
    val image: String?,
    val authorId: String,
)

/**
 * Extension function to convert a Flow of List of ApiRecipe to a Flow of List of Recipe.
 * It maps each ApiRecipe item to a Recipe item.
 *
 * @return A Flow of List of Recipe items.
 */
fun Flow<List<ApiRecipe>>.asDomainObjects(): Flow<List<Recipe>> {
    return map { apiRecipes ->
        apiRecipes.map { it.asDomainObject() }
    }
}

/**
 * Extension function to convert an ApiRecipe item to a Recipe item.
 *
 * @return A Recipe item.
 */
fun ApiRecipe.asDomainObject(): Recipe {
    return Recipe(
        serverId = this.id,
        slug = this.slug,
        title = this.title,
        ingredients = this.ingredients,
        optionalIngredients = this.optionalIngredients,
        herbs = this.herbs,
        steps = this.steps,
        image = this.image,
    )
}