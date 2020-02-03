package com.jordansilva.map4.data.mapper

import com.jordansilva.map4.data.remote.foursquare.model.VenueResponse
import com.jordansilva.map4.domain.model.POI
import java.lang.Exception

object VenuePoiMapper : Mapper<VenueResponse, POI> {
    override fun mapToDomain(response: VenueResponse): POI {
        return POI(
            id = response.id,
            name = response.name,
            description = response.description,
            category = response.categories.firstOrNull { it.primary }?.shortName,
            latitude = response.location.lat ?: 0.0,
            longitude = response.location.lng ?: 0.0,
            rating = response.rating
        )
    }
}