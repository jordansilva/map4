package com.jordansilva.map4.data.device

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getLocation(): Flow<Location>
}