package com.jordansilva.map4.data.remote.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.jordansilva.map4.data.remote.exception.NetworkApiException
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class NetworkConnectionInterceptor(private var applicationContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isInternetAvailable(applicationContext)) {
            throw NetworkApiException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun isInternetAvailable(applicationContext: Context): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> getNetworkInfoSDK23(connectivityManager)
            else -> getNetworkInfoSDK21(connectivityManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getNetworkInfoSDK23(connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun getNetworkInfoSDK21(connectivityManager: ConnectivityManager): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}