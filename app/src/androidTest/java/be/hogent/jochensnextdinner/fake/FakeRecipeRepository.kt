package be.hogent.jochensnextdinner.fake

import be.hogent.jochensnextdinner.data.RecipeRepository
import be.hogent.jochensnextdinner.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRecipeRepository : RecipeRepository {
    private val recipes = mutableListOf(
        Recipe(
            localId = 1,
            serverId = "1",
            slug = "pad-thai-a-la-jochen-4p",
            title = "Pad thai à la Jochen 4p",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            optionalIngredients = listOf("Optional Ingredient 1"),
            herbs = listOf("Herb 1"),
            steps = listOf("Step 1", "Step 2"),
            image = "c_limit,w_600/f_auto/q_auto/v1/jochens-next-dinner/recipes/received_810378936715343_zuiest.jpg?_a=BAVAS0Gd0"
        )
    )

    override fun getRecipes(): Flow<List<Recipe>> {
        return flow {
            emit(recipes)
        }
    }

    override fun getRecipe(id: Long): Flow<Recipe?> {
        return flow {
            emit(
                Recipe(
                    localId = 1,
                    serverId = "1",
                    slug = "pad-thai-a-la-jochen-4p",
                    title = "Pad thai à la Jochen 4p",
                    ingredients = listOf("Ingredient 1", "Ingredient 2"),
                    optionalIngredients = listOf("Optional Ingredient 1"),
                    herbs = listOf("Herb 1"),
                    steps = listOf("Step 1", "Step 2"),
                    image = "c_limit,w_600/f_auto/q_auto/v1/jochens-next-dinner/recipes/received_810378936715343_zuiest.jpg?_a=BAVAS0Gd0"
                )
            )
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(recipe: Recipe): Recipe {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    // Implement other methods as needed...
}