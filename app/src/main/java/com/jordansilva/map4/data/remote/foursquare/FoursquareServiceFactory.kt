package com.jordansilva.map4.data.remote.foursquare

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jordansilva.map4.data.remote.ServiceFactory
import com.jordansilva.map4.data.remote.interceptor.HttpFoursquareInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object FoursquareServiceFactory : ServiceFactory() {

    private const val FOURSQUARE_API = "https://api.foursquare.com/v2/"

    fun venuesApi() = retrofit().create(VenuesApi::class.java)

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(FOURSQUARE_API)
        .addConverterFactory(makeGsonFactory())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(baseClient())
        .build()

    private fun baseClient() = OkHttpClient.Builder()
        //.addInterceptor(makeLoggingInterceptor(true))
        //.addInterceptor(httpLoggingInterceptor)
        //.addInterceptor(NetworkConnectionInterceptor())
        .addInterceptor(HttpFoursquareInterceptor())
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()
}

