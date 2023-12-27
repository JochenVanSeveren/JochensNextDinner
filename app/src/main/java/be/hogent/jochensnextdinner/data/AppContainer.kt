package be.hogent.jochensnextdinner.data

import android.content.Context
import android.util.Log
import be.hogent.jochensnextdinner.BuildConfig
import be.hogent.jochensnextdinner.data.database.CantEatDb
import be.hogent.jochensnextdinner.network.CantEatApiService
import be.hogent.jochensnextdinner.network.NetworkConnectionInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.Type

interface AppContainer {
    val cantEatRepository: CantEatRepository
    //val likesRepository: LikesRepository
    //val recipceRepository: RecipceRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl = BuildConfig.BASE_URL
    private val apiKey = BuildConfig.API_KEY

    private val networkCheck = NetworkConnectionInterceptor(context)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(networkCheck)
        .addInterceptor(ApiKeyInterceptor(apiKey))
        .apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
//                    TODO remove logging because this may contain sensitive data
                    level = HttpLoggingInterceptor.Level.BODY
                    redactHeader("Authorization")
                    redactHeader("Cookie")
                }
                addInterceptor(loggingInterceptor)
            }
        }
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//        API sometimes returns 500 error, so I needed a retry method
        .addCallAdapterFactory(RetryCallAdapterFactory())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: CantEatApiService by lazy {
        retrofit.create(CantEatApiService::class.java)
    }

    override val cantEatRepository: CantEatRepository by lazy {
        CachingCantEatRepository(
            CantEatDb.getDatabase(context = context).cantEatDao(),
            retrofitService,
            context
        )
    }
}

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("x-api-key", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}

/**
 * Factory for creating call adapters that support retrying API calls.
 */
class RetryCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) {
            return null
        }
        val delegate = retrofit.nextCallAdapter(this, returnType, annotations)
        return RetryCallAdapter(delegate)
    }
}

/**
 * Call adapter that delegates to another call adapter and supports retrying API calls.
 *
 * @property delegate The call adapter to delegate to.
 */
class RetryCallAdapter<R>(
    private val delegate: CallAdapter<R, *>
) : CallAdapter<R, Any> {

    override fun responseType(): Type {
        return delegate.responseType()
    }

    override fun adapt(call: Call<R>): Any {
        return when (val adaptedCall = delegate.adapt(call)) {
            is Call<*> -> RetryCall(adaptedCall)
            else -> adaptedCall
        }
    }
}

/**
 * Call that delegates to another call and supports retrying API calls.
 *
 * @property delegate The call to delegate to.
 */
class RetryCall<R>(
    private val delegate: Call<R>
) : Call<R> by delegate {

    private val maxRetries = 3

    override fun enqueue(callback: Callback<R>) {
        delegate.enqueue(object : Callback<R> {
            var retryCount = 0

            override fun onResponse(call: Call<R>, response: Response<R>) {
                if (!response.isSuccessful && response.code() == 500 && retryCount < maxRetries) {
                    retryCount++
                    Log.d("RetryCall", "Retrying request. Attempt: $retryCount")
                    delegate.clone().enqueue(this)
                } else {
                    callback.onResponse(call, response)
                }
            }

            override fun onFailure(call: Call<R>, t: Throwable) {
                if (retryCount < maxRetries) {
                    retryCount++
                    Log.d("RetryCall", "Retrying request. Attempt: $retryCount")
                    delegate.clone().enqueue(this)
                } else {
                    callback.onFailure(call, t)
                }
            }
        })
    }


    override fun clone(): Call<R> {
        return RetryCall(delegate.clone())
    }
}
