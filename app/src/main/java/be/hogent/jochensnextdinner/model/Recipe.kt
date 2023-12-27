package be.hogent.jochensnextdinner.model

import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.dbRecipe
import be.hogent.jochensnextdinner.network.ApiRecipe

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