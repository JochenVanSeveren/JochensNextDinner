package be.hogent.jochensnextdinner.data

import be.hogent.jochensnextdinner.network.CantEatService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val jochensNextDinnerRepository: JochensNextDinnerRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://192.168.145.86:3000/api/"
//    private val baseUrl = "https://jochens-next-js-dinner.vercel.app/api/"


    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: CantEatService by lazy {
        retrofit.create(CantEatService::class.java)
    }

    override val jochensNextDinnerRepository: JochensNextDinnerRepository by lazy {
        NetworkJochensNextDinnerRepository(retrofitService)
    }
}