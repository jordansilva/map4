package com.jordansilva.map4.data.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

open class ServiceFactory {

    protected fun makeGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    protected fun makeGsonFactory(): GsonConverterFactory = GsonConverterFactory.create(makeGson())

    protected fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
}