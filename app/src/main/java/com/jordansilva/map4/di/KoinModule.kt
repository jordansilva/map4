package com.jordansilva.map4.di

import com.jordansilva.map4.data.device.LocationProvider
import com.jordansilva.map4.domain.repository.LocationRepository
import com.jordansilva.map4.data.LocationRepositoryImpl
import com.jordansilva.map4.data.POIRepositoryImpl
import com.jordansilva.map4.data.remote.foursquare.FoursquareServiceFactory
import com.jordansilva.map4.data.remote.foursquare.VenuesApi
import com.jordansilva.map4.data.remote.interceptor.HttpFoursquareInterceptor
import com.jordansilva.map4.data.remote.interceptor.NetworkConnectionInterceptor
import com.jordansilva.map4.domain.repository.POIRepository
import com.jordansilva.map4.domain.usecase.location.GetLocationUseCase
import com.jordansilva.map4.domain.usecase.poi.GetPlacesByAreaUseCase
import com.jordansilva.map4.ui.map.LocationViewModel
import com.jordansilva.map4.ui.map.POIViewModel
import com.jordansilva.map4.util.AndroidLocationProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModule {
    val ViewModule = module {
        //POI details and POIs list viewModel injection
        viewModel { POIViewModel(getNearbyPlacesUseCase = get()) }
        viewModel { LocationViewModel(getLocationUseCase = get()) }
    }

    val UseCaseModule = module {
        factory { GetPlacesByAreaUseCase(repository = get()) }
        factory { GetLocationUseCase(locationRepository = get()) }
        //factory { GetPOIUseCase(get()) }

        //Gson instance
        //single { GsonFactory.getInstance() }
    }

    val RepositoryModule = module {
        single<LocationProvider> { AndroidLocationProvider(androidApplication()) }

        factory<POIRepository> { POIRepositoryImpl(venuesApi = get()) }
        factory<LocationRepository> { LocationRepositoryImpl(locationProvider = get()) }
    }

    val ApiModule = module {
        factory { NetworkConnectionInterceptor(applicationContext = androidApplication()) }
        factory { HttpFoursquareInterceptor() }

        //APIs
        factory<VenuesApi> { FoursquareServiceFactory.venuesApi() }
    }
}