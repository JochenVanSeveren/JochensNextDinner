package be.hogent.jochensnextdinner.model

import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.dbRecipe
import be.hogent.jochensnextdinner.network.ApiRecipe

/**
 * This class represents a Recipe entity.
 *
 * @property localId The local ID of the Recipe.
 * @property serverId The server ID of the Recipe.
 * @property slug The slug of the Recipe.
 * @property title The title of the Recipe.
 * @property ingredients The ingredients of the Recipe.
 * @property optionalIngredients The optional ingredients of the Recipe.
 * @property herbs The herbs of the Recipe.
 * @property steps The steps of the Recipe.
 * @property image The image of the Recipe.
 */
data class Recipe(
    val localId: Long = 0,
    val serverId: String? = null,
    val slug: String,
    val title: String,
    val ingredients: List<String>,
    val optionalIngredients: List<String>,
    val herbs: List<String>,
    val steps: List<String>,
    val image: String?,
) {
    /**
     * Converts this Recipe to a dbRecipe.
     *
     * @return The dbRecipe representation of this Recipe.
     */
    fun asDbRecipe(): dbRecipe {
        return dbRecipe(
            localId = this.localId,
            serverId = this.serverId,
            slug = this.slug,
            title = this.title,
            ingredients = this.ingredients,
            optionalIngredients = this.optionalIngredients,
            herbs = this.herbs,
            steps = this.steps,
            image = this.image
        )
    }

    /**
     * Converts this Recipe to an ApiRecipe.
     *
     * @return The ApiRecipe representation of this Recipe.
     */
    fun asApiObject(): ApiRecipe {
        return if (this.serverId != null) {
            ApiRecipe(
                id = this.serverId,
                slug = this.slug,
                title = this.title,
                ingredients = this.ingredients,
                optionalIngredients = this.optionalIngredients,
                herbs = this.herbs,
                steps = this.steps,
                image = this.image,
                authorId = BuildConfig.AUTHOR_ID,
            )
        } else {
            ApiRecipe(
                slug = this.slug,
                title = this.title,
                ingredients = this.ingredients,
                optionalIngredients = this.optionalIngredients,
                herbs = this.herbs,
                steps = this.steps,
                image = this.image,
                authorId = BuildConfig.AUTHOR_ID,
            )
        }
    }
}