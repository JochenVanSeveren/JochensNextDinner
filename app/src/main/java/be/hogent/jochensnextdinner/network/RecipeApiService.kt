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

interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(): List<ApiRecipe>

    @POST("recipes")
    suspend fun postRecipe(@Body recipe: ApiRecipe): ApiRecipe

    @PUT("recipes/{id}")
    suspend fun putRecipe(@Path("id") id: String, @Body recipe: ApiRecipe): ApiRecipe

    @DELETE("recipes/{id}")
    suspend fun deleteRecipe(@Path("id") id: String): ApiRecipe
}

fun RecipeApiService.getRecipesAsFlow(): Flow<List<ApiRecipe>> = flow {
    try {
        emit(getRecipes())
    } catch (e: Exception) {
        Log.e("API", "getRecipesAsFlow: " + e.stackTraceToString())
    }
}

fun RecipeApiService.postRecipeAsFlow(recipe: ApiRecipe): Flow<ApiRecipe> = flow {
    try {
        Log.d("API", "postRecipeAsFlow: $recipe")
        emit(postRecipe(recipe))
    } catch (e: Exception) {
        Log.e("API", "postRecipeAsFlow: " + e.stackTraceToString())
    }
}

fun RecipeApiService.putRecipeAsFlow(recipe: ApiRecipe): Flow<ApiRecipe> = flow {
    try {
        Log.d("API", "putRecipeAsFlow: $recipe")
        emit(putRecipe(recipe.id!!, recipe))
    } catch (e: Exception) {
        Log.e("API", "putRecipeAsFlow: " + e.stackTraceToString())
    }
}

fun RecipeApiService.deleteRecipeAsFlow(id: String): Flow<ApiRecipe> = flow {
    try {
        emit(deleteRecipe(id))
    } catch (e: Exception) {
        Log.e("API", "deleteRecipeAsFlow: " + e.stackTraceToString())
    }
}