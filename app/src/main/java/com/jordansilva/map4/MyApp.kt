package com.jordansilva.map4

import android.app.Application
import com.jordansilva.map4.di.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        iniKoin()
    }

    private fun iniKoin() {
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    KoinModule.ViewModule,
                    KoinModule.UseCaseModule,
                    KoinModule.RepositoryModule,
                    KoinModule.ApiModule
                )
            )
        }
    }

}