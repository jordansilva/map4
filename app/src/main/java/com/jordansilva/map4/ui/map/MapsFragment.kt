package com.jordansilva.map4.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.jordansilva.map4.R
import com.jordansilva.map4.ui.model.LocationViewData
import com.jordansilva.map4.ui.model.POIViewData
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : Fragment(R.layout.fragment_maps) {

    companion object {
        fun newInstance() = MapsFragment()
    }

    private val poiViewModel: POIViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()

    private var googleMap: GoogleMap? = null
    private lateinit var clusterMapItem: ClusterManager<GoogleMapItem>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initMap()
        poiViewModel.poiList.observe(viewLifecycleOwner, Observer { addMakers(it) })

    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            setupMap()
        }
    }

    private fun setupMap() {
        googleMap?.apply {
            uiSettings.setAllGesturesEnabled(true)

            clusterMapItem = ClusterManager(requireContext(), this)
            setOnMarkerClickListener(clusterMapItem)
            setOnCameraIdleListener {
                renderNearbyPlaces(projection.visibleRegion.latLngBounds)
                clusterMapItem.onCameraIdle()
            }

            if (checkPermission()) {
                locationViewModel.location.observe(viewLifecycleOwner, Observer { handleLocation(it) })
            }
        }
    }

    private fun renderNearbyPlaces(location: LatLngBounds) {
        poiViewModel.getNearbyPlaces(
            latitudeSW = location.southwest.latitude,
            longitudeSW = location.southwest.longitude,
            latitudeNE = location.northeast.latitude,
            longitudeNE = location.northeast.longitude
        )
    }

    private val listMarkers = mutableMapOf<String, MarkerOptions>()

    private fun addMakers(listPOIs: List<POIViewData>) {
        googleMap?.let { map ->
            listPOIs.forEach {
                if (!listMarkers.containsKey(it.id)) {
                    val marker = MarkerOptions().title(it.name).snippet(it.description).position(LatLng(it.latitude, it.longitude))
                    listMarkers[it.id] = marker
                    map.addMarker(marker)
                }
            }
        }
//        clusterMapItem.clearItems()
//        listPOIs.forEach { clusterMapItem.addItem(GoogleMapItem(it.name, it.description, it.latitude, it.longitude)) }
//        clusterMapItem.cluster()
    }

    private fun handleLocation(location: LocationViewData) {
        Log.d("handleLocation", "$location")
    }

    //TODO: Extract to PermissionHelper
    private fun checkPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permissions, 100)
            false
        } else
            true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initMap()
            } else {
                TODO()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}