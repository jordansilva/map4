package com.jordansilva.map4.util

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import com.jordansilva.map4.data.device.LocationProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await

class AndroidLocationProvider(context: Context) : LocationProvider {

    private companion object {
        private const val TIME_INTERVAL = 30000L
        private const val FASTEST_INTERVAL = 10000L
        private const val PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val request = LocationRequest.create()
        .setInterval(TIME_INTERVAL)
        .setFastestInterval(FASTEST_INTERVAL)
        .setPriority(PRIORITY)

    @Throws(SecurityException::class)
    override fun getLocation(): Flow<Location> = channelFlow {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                if (!isClosedForSend) {
                    offer(result.lastLocation)
                }
            }
        }
        client.lastLocation.await<Location?>()?.let { send(it) }
        client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper()).await()
        awaitClose { client.removeLocationUpdates(locationCallback) }
    }
}