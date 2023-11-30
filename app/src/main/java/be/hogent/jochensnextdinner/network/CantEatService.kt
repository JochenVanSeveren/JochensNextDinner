package be.hogent.jochensnextdinner.network

import be.hogent.jochensnextdinner.model.CantEat
import retrofit2.http.*

interface CantEatService {
    @GET("canteats/{id}")
    suspend fun getCantEat(@Path("id") id: String): CantEat

    @GET("canteats")
    suspend fun getAllCantEats(): List<CantEat>

    @POST("canteats")
    suspend fun createCantEat(@Body newCantEat: CantEat): CantEat

    @PUT("canteats/{id}")
    suspend fun updateCantEat(@Path("id") id: String, @Body cantEat: CantEat): CantEat

    @DELETE("canteats/{id}")
    suspend fun deleteCantEat(@Path("id") id: String): CantEat
}

