package be.hogent.jochensnextdinner.network

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.GET

interface CantEatApiService {
    @GET("canteats")
    suspend fun getCantEats(): List<ApiCantEat>

    /*TODO: add others*/
}

fun CantEatApiService.getCantEatsAsFlow(): Flow<List<ApiCantEat>> = flow {
    try {
        emit(getCantEats())
    } catch (e: Exception) {
        Log.e("API", "getCantEatsAsFlow: " + e.stackTraceToString())
    }
}