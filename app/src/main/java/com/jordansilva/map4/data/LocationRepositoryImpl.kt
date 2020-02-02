package com.jordansilva.map4.data

import com.jordansilva.map4.data.device.LocationProvider
import com.jordansilva.map4.domain.model.Location
import com.jordansilva.map4.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(private val locationProvider: LocationProvider) : LocationRepository {

    override fun getLastKnownLocation(): Flow<Location> {
        return locationProvider
            .getLocation()
            .map { Location(it.latitude, it.longitude, it.time) }
    }

}