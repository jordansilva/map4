package com.jordansilva.map4

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.jordansilva.map4.di.KoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()

        iniKoin()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Log.d("MyApp", "App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Log.d("MyApp", "App in foreground")
    }

}