package be.hogent.jochensnextdinner.data

import android.util.Log
import be.hogent.jochensnextdinner.data.database.RecipeDao
import be.hogent.jochensnextdinner.data.database.asDomainObject
import be.hogent.jochensnextdinner.data.database.asDomainRecipes
import be.hogent.jochensnextdinner.model.Recipe
import be.hogent.jochensnextdinner.network.RecipeApiService
import be.hogent.jochensnextdinner.network.asDomainObject
import be.hogent.jochensnextdinner.network.asDomainObjects
import be.hogent.jochensnextdinner.network.deleteRecipeAsFlow
import be.hogent.jochensnextdinner.network.getRecipesAsFlow
import be.hogent.jochensnextdinner.network.postRecipeAsFlow
import be.hogent.jochensnextdinner.network.putRecipeAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException

/**
 * Interface for the RecipeRepository.
 * It contains the methods for managing Recipe data.
 */
interface RecipeRepository {
    fun getRecipes(): Flow<List<Recipe>>
    fun getRecipe(id: Long): Flow<Recipe?>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun saveRecipe(recipe: Recipe): Recipe
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun refresh()
}

/**
 * Implementation of the RecipeRepository interface.
 * It uses a local database and a remote API for managing Recipe data.
 */
class CachingRecipeRepository(
    private val recipeDao: RecipeDao,
    private val recipeApiService: RecipeApiService
) : RecipeRepository {

    /**
     * Fetches all Recipe items from the local database.
     * If the local database is empty, it refreshes the data from the remote API.
     */
    override fun getRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllItems().map {
            it.asDomainRecipes()
        }.onEach { recipes ->
            if (recipes.isEmpty()) {
                refresh()
            }
        }
    }

    /**
     * Fetches a Recipe item by its id from the local database.
     */
    override fun getRecipe(id: Long): Flow<Recipe?> {
        return recipeDao.getItem(id).map {
            it.asDomainObject()
        }
    }

    /**
     * Inserts a Recipe item into the local database.
     */
    override suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insert(recipe.asDbRecipe())
    }

    /**
     * Saves a Recipe item to the remote API and the local database.
     * If the item already exists in the remote API, it updates the item.
     * Otherwise, it creates a new item.
     */
    override suspend fun saveRecipe(recipe: Recipe): Recipe {
        val apiRecipe = recipe.asApiObject()
        val response = if (recipe.serverId != null)
            recipeApiService.putRecipeAsFlow(apiRecipe).first()
        else
            recipeApiService.postRecipeAsFlow(apiRecipe).first()
        val createdRecipe = response.asDomainObject()
        recipeDao.insert(createdRecipe.asDbRecipe())
        return createdRecipe
    }

    /**
     * Deletes a Recipe item from the remote API and the local database.
     */
    override suspend fun deleteRecipe(recipe: Recipe) {
        if (recipe.serverId != null) {
            val apiRecipe = recipe.asApiObject()
            recipeApiService.deleteRecipeAsFlow(apiRecipe.id!!).first()
        }
        recipeDao.delete(recipe.asDbRecipe())
    }

    /**
     * Updates a Recipe item in the remote API and the local database.
     */
    override suspend fun updateRecipe(recipe: Recipe) {
        val apiRecipe = recipe.asApiObject()
        val response = recipeApiService.putRecipeAsFlow(apiRecipe).first()
        recipeDao.update(response.asDomainObject().asDbRecipe())
    }

    /**
     * Refreshes the local database with data fetched from the remote API.
     *
     * This function performs the following steps:
     * 1. Fetches all items from the local database.
     * 2. Fetches all items from the remote API.
     * 3. Compares the local and remote items:
     *    - If an item is present in the local database but not in the remote, it is deleted from the local database.
     *    - If an item is present in the remote but not in the local database, it is added to the local database.
     *    - If an item is present in both the local database and the remote, the local item is updated with the data from the remote.
     *
     * @throws SocketTimeoutException If a timeout occurs while fetching data from the remote API.
     */
    override suspend fun refresh() {
        try {
            val dbRecipes = recipeDao.getAllItems().first().asDomainRecipes()

            recipeApiService.getRecipesAsFlow().asDomainObjects().collect { apiRecipes ->
                val itemsToDelete = dbRecipes.filter { dbRecipe ->
                    apiRecipes.none { apiRecipe ->
                        apiRecipe.serverId == dbRecipe.serverId
                    }
                }

                for (item in itemsToDelete) {
                    recipeDao.delete(item.asDbRecipe())
                }

                for (recipe in apiRecipes) {
                    recipeDao.insert(recipe.asDbRecipe())
                }
            }
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeoutException", e.message ?: "No message")
        }
    }
}