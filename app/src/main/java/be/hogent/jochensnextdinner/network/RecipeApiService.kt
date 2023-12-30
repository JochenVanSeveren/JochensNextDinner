package be.hogent.jochensnextdinner.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interface for the RecipeApiService.
 * It contains the methods for managing Recipe data in the API.
 */
interface RecipeApiService {
    /**
     * Fetches all Recipe items from the API.
     *
     * @return A list of ApiRecipe items.
     */
    @GET("recipes")
    suspend fun getRecipes(): List<ApiRecipe>

    /**
     * Posts a new Recipe item to the API.
     *
     * @param recipe The Recipe item to post.
     * @return The created ApiRecipe item.
     */
    @POST("recipes")
    suspend fun postRecipe(@Body recipe: ApiRecipe): ApiRecipe

    /**
     * Updates a Recipe item in the API.
     *
     * @param id The ID of the Recipe item to update.
     * @param recipe The updated Recipe item.
     * @return The updated ApiRecipe item.
     */
    @PUT("recipes/{id}")
    suspend fun putRecipe(@Path("id") id: String, @Body recipe: ApiRecipe): ApiRecipe

    /**
     * Deletes a Recipe item from the API.
     *
     * @param id The ID of the Recipe item to delete.
     * @return The deleted ApiRecipe item.
     */
    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: String): ApiRecipe
}

/**
 * Extension function to convert the getRecipes function to a Flow.
 *
 * @return A Flow of List of ApiRecipe items.
 */
fun RecipeApiService.getRecipesAsFlow(): Flow<List<ApiRecipe>> = flow {
    try {
        emit(getRecipes())
    } catch (e: Exception) {
        Log.e("API", "getRecipesAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the postRecipe function to a Flow.
 *
 * @param recipe The Recipe item to post.
 * @return A Flow of the created ApiRecipe item.
 */
fun RecipeApiService.postRecipeAsFlow(recipe: ApiRecipe): Flow<ApiRecipe> = flow {
    try {
        Log.d("API", "postRecipeAsFlow: $recipe")
        emit(postRecipe(recipe))
    } catch (e: Exception) {
        Log.e("API", "postRecipeAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the putRecipe function to a Flow.
 *
 * @param recipe The Recipe item to update.
 * @return A Flow of the updated ApiRecipe item.
 */
fun RecipeApiService.putRecipeAsFlow(recipe: ApiRecipe): Flow<ApiRecipe> = flow {
    try {
        Log.d("API", "putRecipeAsFlow: $recipe")
        emit(putRecipe(recipe.id!!, recipe))
    } catch (e: Exception) {
        Log.e("API", "putRecipeAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the deleteRecipe function to a Flow.
 *
 * @param id The ID of the Recipe item to delete.
 * @return A Flow of the deleted ApiRecipe item.
 */
fun RecipeApiService.deleteRecipeAsFlow(id: String): Flow<ApiRecipe> = flow {
    try {
        emit(deleteRecipe(id))
    } catch (e: Exception) {
        Log.e("API", "deleteRecipeAsFlow: " + e.stackTraceToString())
    }
}