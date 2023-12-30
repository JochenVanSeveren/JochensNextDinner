package be.hogent.jochensnextdinner.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * Interceptor class for checking network connection.
 *
 * @property context The application context.
 */
class NetworkConnectionInterceptor(val context: Context) : Interceptor {

    /**
     * Intercepts the chain and checks for network connection.
     * If there is no connection, it throws an IOException.
     * Otherwise, it proceeds with the chain.
     *
     * @param chain The chain to intercept.
     * @return The response from the chain.
     * @throws IOException If there is no network connection.
     */
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        if (!isConnected(context = context)) {
            Log.i("retrofit", "there is no connection")
            throw IOException()
        } else {
            val builder = chain.request().newBuilder()
            return@run chain.proceed(builder.build())
        }
    }

    /**
     * Checks if there is a network connection.
     *
     * @param context The application context.
     * @return True if there is a network connection, false otherwise.
     */
    private fun isConnected(context: Context): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}