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

interface LikeApiService {
    @GET("likes")
    suspend fun getLikes(): List<ApiLike>

    @POST("likes")
    suspend fun postLike(@Body like: ApiLike): ApiLike

    @PUT("likes/{id}")
    suspend fun putLike(@Path("id") id: String, @Body like: ApiLike): ApiLike

    @DELETE("likes/{id}")
    suspend fun deleteLike(@Path("id") id: String): ApiLike
}

fun LikeApiService.getLikesAsFlow(): Flow<List<ApiLike>> = flow {
    try {
        emit(getLikes())
    } catch (e: Exception) {
        Log.e("API", "getLikesAsFlow: " + e.stackTraceToString())
    }
}

fun LikeApiService.postLikeAsFlow(like: ApiLike): Flow<ApiLike> = flow {
    try {
        Log.d("API", "postLikeAsFlow: $like")
        emit(postLike(like))
    } catch (e: Exception) {
        Log.e("API", "postLikeAsFlow: " + e.stackTraceToString())
    }
}

fun LikeApiService.putLikeAsFlow(like: ApiLike): Flow<ApiLike> = flow {
    try {
        Log.d("API", "putLikeAsFlow: $like")
        emit(putLike(like.id!!, like))
    } catch (e: Exception) {
        Log.e("API", "putLikeAsFlow: " + e.stackTraceToString())
    }
}

fun LikeApiService.deleteLikeAsFlow(id: String): Flow<ApiLike> = flow {
    try {
        emit(deleteLike(id))
    } catch (e: Exception) {
        Log.e("API", "deleteLikeAsFlow: " + e.stackTraceToString())
    }
}