package be.hogent.jochensnextdinner.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.utils.Converters

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