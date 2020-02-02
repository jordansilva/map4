package com.jordansilva.map4.data

import com.jordansilva.map4.data.mapper.VenuePoiMapper
import com.jordansilva.map4.data.remote.foursquare.VenuesApi
import com.jordansilva.map4.domain.model.Location
import com.jordansilva.map4.domain.model.POI
import com.jordansilva.map4.domain.repository.POIRepository

class POIRepositoryImpl(private val venuesApi: VenuesApi) : BaseRepository(), POIRepository {

    override suspend fun getNearbyPlaces(location: Location, radius: Int): List<POI> {
        return executeSafe {
            val result = venuesApi
                .searchByCoordinates(coordinates = location.asString(), intent = "checkin", radius = radius, limit = 50)
                .await()

            if (result.isSuccess()) {
                return@executeSafe result.response.result.map { VenuePoiMapper.mapToDomain(response = it) }
            } else {
                return@executeSafe emptyList()
            }
        }
    }

    override suspend fun getNearbyPlacesByArea(southWest: Location, northEast: Location): List<POI> {
        return executeSafe {
            val result = venuesApi.searchByBoundingBox(
                southWest = southWest.asString(),
                northEast = northEast.asString(),
                query = "restaurant"
            ).await()

            if (result.isSuccess()) {
                return@executeSafe result.response.result.map { VenuePoiMapper.mapToDomain(response = it) }
            } else {
                return@executeSafe emptyList()
            }
        }
    }


    override suspend fun getPlace(id: String): POI {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

