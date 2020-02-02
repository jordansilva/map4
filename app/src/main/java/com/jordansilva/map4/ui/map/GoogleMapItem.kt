package com.jordansilva.map4.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class GoogleMapItem(
    private val title: String,
    private val snippet: String?,
    private val latitude: Double,
    private val longitude: Double
) : ClusterItem {
    override fun getTitle(): String = title
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
    override fun getSnippet(): String = snippet ?: ""
}
