package be.hogent.jochensnextdinner.data

import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.network.CantEatService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

interface AppContainer {
    val jochensNextDinnerRepository: JochensNextDinnerRepository
}

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("x-api-key", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = BuildConfig.BASE_URL
    private val apiKey = BuildConfig.API_KEY

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor(apiKey))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
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