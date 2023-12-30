package be.hogent.jochensnextdinner.network

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.*

/**
 * Interface for the CantEatApiService.
 * It contains the methods for managing CantEat data in the API.
 */
interface CantEatApiService {
    /**
     * Fetches all CantEat items from the API.
     *
     * @return A list of ApiCantEat items.
     */
    @GET("canteats")
    suspend fun getCantEats(): List<ApiCantEat>

    /**
     * Posts a new CantEat item to the API.
     *
     * @param cantEat The CantEat item to post.
     * @return The created ApiCantEat item.
     */
    @POST("canteats")
    suspend fun postCantEat(@Body cantEat: ApiCantEat): ApiCantEat

    /**
     * Updates a CantEat item in the API.
     *
     * @param id The ID of the CantEat item to update.
     * @param cantEat The updated CantEat item.
     * @return The updated ApiCantEat item.
     */
    @PUT("canteats/{id}")
    suspend fun putCantEat(@Path("id") id: String, @Body cantEat: ApiCantEat): ApiCantEat

    /**
     * Deletes a CantEat item from the API.
     *
     * @param id The ID of the CantEat item to delete.
     * @return The deleted ApiCantEat item.
     */
    @DELETE("canteats/{id}")
    suspend fun deleteCantEat(@Path("id") id: String): ApiCantEat
}

/**
 * Extension function to convert the getCantEats function to a Flow.
 *
 * @return A Flow of List of ApiCantEat items.
 */
fun CantEatApiService.getCantEatsAsFlow(): Flow<List<ApiCantEat>> = flow {
    try {
        emit(getCantEats())
    } catch (e: Exception) {
        Log.e("API", "getCantEatsAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the postCantEat function to a Flow.
 *
 * @param cantEat The CantEat item to post.
 * @return A Flow of the created ApiCantEat item.
 */
fun CantEatApiService.postCantEatAsFlow(cantEat: ApiCantEat): Flow<ApiCantEat> = flow {
    try {
        Log.d(TAG, "postCantEatAsFlow: $cantEat")
        emit(postCantEat(cantEat))
    } catch (e: Exception) {
        Log.e("API", "postCantEatAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the putCantEat function to a Flow.
 *
 * @param cantEat The CantEat item to update.
 * @return A Flow of the updated ApiCantEat item.
 */
fun CantEatApiService.putCantEatAsFlow(cantEat: ApiCantEat): Flow<ApiCantEat> = flow {
    try {
        Log.d(TAG, "putCantEatAsFlow: $cantEat")
        emit(putCantEat(cantEat.id!!, cantEat))
    } catch (e: Exception) {
        Log.e("API", "putCantEatAsFlow: " + e.stackTraceToString())
    }
}

/**
 * Extension function to convert the deleteCantEat function to a Flow.
 *
 * @param id The ID of the CantEat item to delete.
 * @return A Flow of the deleted ApiCantEat item.
 */
fun CantEatApiService.deleteCantEatAsFlow(id: String): Flow<ApiCantEat> = flow {
    try {
        emit(deleteCantEat(id))
    } catch (e: Exception) {
        Log.e("API", "deleteCantEatAsFlow: " + e.stackTraceToString())
    }
}