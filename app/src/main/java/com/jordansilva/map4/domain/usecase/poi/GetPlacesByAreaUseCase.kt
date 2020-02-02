package com.jordansilva.map4.domain.usecase.poi

import com.jordansilva.map4.domain.model.Location
import com.jordansilva.map4.domain.model.POI
import com.jordansilva.map4.domain.repository.POIRepository
import com.jordansilva.map4.domain.usecase.UseCase
import com.jordansilva.map4.util.UseCaseResult

class GetPlacesByAreaUseCase(private val repository: POIRepository) : UseCase<GetPlacesByAreaRequest, List<POI>>() {

    override suspend fun doWork(params: GetPlacesByAreaRequest): UseCaseResult<List<POI>> {
        val data = repository.getNearbyPlacesByArea(
            southWest = Location(params.latitudeSW, params.longitudeSW),
            northEast = Location(params.latitudeNE, params.longitudeNE)
        )
        return UseCaseResult.Success(data)
    }
}

data class GetPlacesByAreaRequest(
    val latitudeSW: Double, val longitudeSW: Double,
    val latitudeNE: Double, val longitudeNE: Double
)