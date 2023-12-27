package be.hogent.jochensnextdinner.network

import be.hogent.jochensnextdinner.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

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

fun Flow<List<ApiRecipe>>.asDomainObjects(): Flow<List<Recipe>> {
    return map { apiRecipes ->
        apiRecipes.map { it.asDomainObject() }
    }
}

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