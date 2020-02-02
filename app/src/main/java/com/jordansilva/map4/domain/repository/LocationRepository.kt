package com.jordansilva.map4.domain.repository

import com.jordansilva.map4.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLastKnownLocation(): Flow<Location>
}