package com.jordansilva.map4.domain.usecase.poi

import com.jordansilva.map4.domain.model.Location
import com.jordansilva.map4.domain.model.POI
import com.jordansilva.map4.domain.repository.POIRepository
import com.jordansilva.map4.domain.usecase.UseCase
import com.jordansilva.map4.util.UseCaseResult

class GetNearbyPlacesUseCase(private val repository: POIRepository) : UseCase<GetNearbyPlacesRequest, List<POI>>() {

    override suspend fun doWork(params: GetNearbyPlacesRequest): UseCaseResult<List<POI>> {
        val data = repository.getNearbyPlaces(Location(params.latitude, params.longitude), 100)
        return UseCaseResult.Success(data)
    }
}

data class GetNearbyPlacesRequest(val latitude: Double, val longitude: Double)