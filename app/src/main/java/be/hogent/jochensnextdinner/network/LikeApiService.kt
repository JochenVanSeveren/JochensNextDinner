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
 * Interface for the LikeApiService.
 * It contains the methods for managing Like data in the API.
 */
interface LikeApiService {
    /**
     * Fetches all Like items from the API.
     *
     * @return A list of ApiLike items.
     */
    @GET("likes")
    suspend fun getLikes(): List<ApiLike>

    /**
     * Posts a new Like item to the API.
     *
     * @param like The Like item to post.
     * @return The created ApiLike item.
     */
    @POST("likes")
    suspend fun postLike(@Body like: ApiLike): ApiLike

    /**
     * Updates a Like item in the API.
     *
     * @param id The ID of the Like item to update.
     * @param like The updated Like item.
     * @return The updated ApiLike item.
     */
    @PUT("likes/{id}")
    suspend fun putLike(@Path("id") id: String, @Body like: ApiLike): ApiLike

    /**
     * Deletes a Like item from the API.
     *
     * @param id The ID of the Like item to delete.
     * @return The deleted ApiLike item.
     */
    @DELETE("likes/{id}")
    suspend fun deleteLike(@Path("id") id: String): ApiLike
}

/**
 * Extension function to convert the getLikes function to a Flow.
 *
 * @return A Flow of List of ApiLike items.
 */
fun LikeApiService.getLikesAsFlow(): Flow<List<ApiLike>> = flow {
    try {
        emit(getLikes())
    } catch (e: Exception) {
        Log.e("API", "getLikesAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the postLike function to a Flow.
 *
 * @param like The Like item to post.
 * @return A Flow of the created ApiLike item.
 */
fun LikeApiService.postLikeAsFlow(like: ApiLike): Flow<ApiLike> = flow {
    try {
        Log.d("API", "postLikeAsFlow: $like")
        emit(postLike(like))
    } catch (e: Exception) {
        Log.e("API", "postLikeAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the putLike function to a Flow.
 *
 * @param like The Like item to update.
 * @return A Flow of the updated ApiLike item.
 */
fun LikeApiService.putLikeAsFlow(like: ApiLike): Flow<ApiLike> = flow {
    try {
        Log.d("API", "putLikeAsFlow: $like")
        emit(putLike(like.id!!, like))
    } catch (e: Exception) {
        Log.e("API", "putLikeAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the deleteLike function to a Flow.
 *
 * @param id The ID of the Like item to delete.
 * @return A Flow of the deleted ApiLike item.
 */
fun LikeApiService.deleteLikeAsFlow(id: String): Flow<ApiLike> = flow {
    try {
        emit(deleteLike(id))
    } catch (e: Exception) {
        Log.e("API", "deleteLikeAsFlow: " + e.stackTraceToString())
    }
}