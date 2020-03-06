package com.jordansilva.map4.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.jordansilva.map4.R
import com.jordansilva.map4.databinding.FragmentMapsBinding
import com.jordansilva.map4.ui.model.LocationViewData
import com.jordansilva.map4.ui.model.POIViewData
import com.jordansilva.map4.ui.poidetail.POIDetailFragment
import com.jordansilva.map4.util.extensions.displayCircularReveal
import com.jordansilva.map4.util.extensions.visible
import com.jordansilva.map4.util.permissions.askPermission
import com.jordansilva.map4.util.permissions.checkPermission
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.map_item_card.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsFragment : Fragment(R.layout.fragment_maps) {

    companion object {
        private const val PERMISSION_CODE = 100
        fun newInstance() = MapsFragment()
    }

    private val poiViewModel: POIViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()

    private var googleMap: GoogleMap? = null
    private val listMarkers = mutableMapOf<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMapsBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.poiViewModel = poiViewModel

        initUi()
    }

    private fun initUi() {
        initMap()
        poiViewModel.poiList.observe(viewLifecycleOwner, Observer { addMakers(it) })
        poiViewModel.poiDetail.observe(viewLifecycleOwner, Observer { showItemInfo(it) })
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            map.apply {
                //Map UI Settings
                uiSettings.setAllGesturesEnabled(true)
                uiSettings.isMyLocationButtonEnabled = false
                uiSettings.isMapToolbarEnabled = false

                //Map Events
                setOnCameraIdleListener { getNearbyPlaces(projection.visibleRegion.latLngBounds) }
                setOnMapClickListener { hideItemInfo() }
                setOnMarkerClickListener {
                    listMarkers[it.id]?.let { poiId -> poiViewModel.getPlace(poiId) }
                    true
                }
                googleMap = this
            }

            enableMapLocation()
        }
    }

    private fun enableMapLocation() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (checkPermission(*permissions)) {
            googleMap?.isMyLocationEnabled = true
            locationViewModel.location.observe(viewLifecycleOwner, Observer { handleNewLocation(it) })
        } else {
            askPermission(*permissions, requestCode = PERMISSION_CODE)
        }
    }

    private fun moveCameraLocation(latitude: Double, longitude: Double) {
        val camera = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 14.0f)
        googleMap?.animateCamera(camera, 300, null)
    }

    private fun showItemInfo(poi: POIViewData): Boolean {
        popupItem.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, POIDetailFragment.newInstance())
                .commitNow()
        }
        return true
    }


    private fun hideItemInfo() {
        popupItem.visible = false
    }

    private fun getNearbyPlaces(location: LatLngBounds) {
        poiViewModel.getNearbyPlaces(
            latitudeSW = location.southwest.latitude,
            longitudeSW = location.southwest.longitude,
            latitudeNE = location.northeast.latitude,
            longitudeNE = location.northeast.longitude
        )
    }

    private fun addMakers(items: List<POIViewData>) {
        googleMap?.let { map ->
            items.forEach { poi ->
                if (!listMarkers.values.contains(poi.id)) {
                    val markerOptions = makeMarker(poi)
                    val marker = map.addMarker(markerOptions)
                    listMarkers[marker.id] = poi.id
                }
            }
        }
    }

    private fun makeMarker(item: POIViewData): MarkerOptions {
        return MarkerOptions()
            .position(LatLng(item.latitude, item.longitude))
            .title(item.name)
    }

    private fun handleNewLocation(location: LocationViewData) {
        moveCameraLocation(location.latitude, location.longitude)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMapLocation()
            } else {
                TODO()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}