package be.hogent.jochensnextdinner.network

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.*

interface CantEatApiService {
    @GET("canteats")
    suspend fun getCantEats(): List<ApiCantEat>

    @POST("canteats")
    suspend fun postCantEat(@Body cantEat: ApiCantEat): ApiCantEat

    @PUT("canteats/{id}")
    suspend fun putCantEat(@Path("id") id: String, @Body cantEat: ApiCantEat): ApiCantEat

    @DELETE("canteats/{id}")
    suspend fun deleteCantEat(@Path("id") id: String): ApiCantEat
}

fun CantEatApiService.getCantEatsAsFlow(): Flow<List<ApiCantEat>> = flow {
    try {
        emit(getCantEats())
    } catch (e: Exception) {
        Log.e("API", "getCantEatsAsFlow: " + e.stackTraceToString())
    }
}

fun CantEatApiService.postCantEatAsFlow(cantEat: ApiCantEat): Flow<ApiCantEat> = flow {
    try {
        Log.d(TAG, "postCantEatAsFlow: $cantEat")
        emit(postCantEat(cantEat))
    } catch (e: Exception) {
        Log.e("API", "postCantEatAsFlow: " + e.stackTraceToString())
    }
}

fun CantEatApiService.putCantEatAsFlow(cantEat: ApiCantEat): Flow<ApiCantEat> = flow {
    try {
        emit(putCantEat(cantEat.id!!, cantEat))
    } catch (e: Exception) {
        Log.e("API", "putCantEatAsFlow: " + e.stackTraceToString())
    }
}

fun CantEatApiService.deleteCantEatAsFlow(id: String): Flow<ApiCantEat> = flow {
    try {
        emit(deleteCantEat(id))
    } catch (e: Exception) {
        Log.e("API", "deleteCantEatAsFlow: " + e.stackTraceToString())
    }
}