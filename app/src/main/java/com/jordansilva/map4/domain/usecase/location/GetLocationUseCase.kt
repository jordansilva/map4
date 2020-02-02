package com.jordansilva.map4.domain.usecase.location

import com.jordansilva.map4.domain.repository.LocationRepository
import com.jordansilva.map4.domain.model.Location
import com.jordansilva.map4.domain.usecase.StreamUseCase
import kotlinx.coroutines.flow.*

class GetLocationUseCase(private val locationRepository: LocationRepository) : StreamUseCase<Unit, Location>() {

    override fun doWork(params: Unit): Flow<Location> {
        return locationRepository.getLastKnownLocation()
            //.distinctUntilChanged { old, new -> old.latitude.equals(new.latitude) && old.longitude.equals(new.longitude) }
    }

}