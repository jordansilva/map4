package com.jordansilva.map4.domain.repository

import com.jordansilva.map4.domain.model.Location
import com.jordansilva.map4.domain.model.POI

interface POIRepository {
    suspend fun getNearbyPlaces(location: Location, radius: Int): List<POI>
    suspend fun getNearbyPlacesByArea(southWest: Location, northEast: Location): List<POI>
    suspend fun getPlace(id: String): POI
}