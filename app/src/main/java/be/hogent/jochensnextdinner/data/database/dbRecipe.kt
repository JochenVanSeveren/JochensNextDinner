package be.hogent.jochensnextdinner.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.utils.Converters

/**
 * Represents a "recipe" in the database.
 *
 * @property localId The unique ID for this recipe in the local database. This is the primary key.
 * @property serverId The unique ID for this recipe on the server. This is indexed and must be unique.
 * @property slug The slug of the recipe.
 * @property title The title of the recipe.
 * @property ingredients The list of ingredients for the recipe.
 * @property optionalIngredients The list of optional ingredients for the recipe.
 * @property herbs The list of herbs for the recipe.
 * @property steps The list of steps for the recipe.
 * @property image The image of the recipe.
 */
@Entity(tableName = "recipes", indices = [Index(value = ["serverId"], unique = true)])
@TypeConverters(Converters::class)
data class dbRecipe(
    @PrimaryKey(autoGenerate = true)
    val localId: Long,
    val serverId: String? = null,
    val slug: String,
    val title: String,
    val ingredients: List<String>,
    val optionalIngredients: List<String>,
    val herbs: List<String>,
    val steps: List<String>,
    val image: String?,
)

/**
 * Converts a list of dbRecipe objects to a list of Recipe domain objects.
 *
 * @return A list of Recipe domain objects.
 */
fun List<dbRecipe>.asDomainRecipes(): List<Recipe> {
    return this.map {
        Recipe(
            localId = it.localId,
            serverId = it.serverId,
            slug = it.slug,
            title = it.title,
            ingredients = it.ingredients,
            optionalIngredients = it.optionalIngredients,
            herbs = it.herbs,
            steps = it.steps,
            image = it.image,
        )
    }
}

/**
 * Converts a dbRecipe object to a Recipe domain object.
 *
 * @return A Recipe domain object.
 */
fun dbRecipe.asDomainObject(): Recipe {
    return Recipe(
        localId = this.localId,
        serverId = this.serverId,
        slug = this.slug,
        title = this.title,
        ingredients = this.ingredients,
        optionalIngredients = this.optionalIngredients,
        herbs = this.herbs,
        steps = this.steps,
        image = this.image,
    )
}