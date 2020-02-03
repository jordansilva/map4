package com.jordansilva.map4.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jordansilva.map4.R
import com.jordansilva.map4.ui.model.LocationViewData
import com.jordansilva.map4.ui.model.POIViewData
import com.jordansilva.map4.ui.poidetail.POIDetailFragment
import com.jordansilva.map4.util.extensions.displayCircularReveal
import com.jordansilva.map4.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.map_item_card.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsFragment : Fragment(R.layout.fragment_maps) {

    companion object {
        fun newInstance() = MapsFragment()
    }

    private val poiViewModel: POIViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()

    private var googleMap: GoogleMap? = null
    private val listMarkers = mutableMapOf<String, MarkerOptions>()
    private val listMarkersId = mutableMapOf<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    listMarkersId[it.id]?.let { poiId -> poiViewModel.getPlace(poiId) }
                    true
                }
                googleMap = this
            }

            enableMapLocation()
        }
    }

    private fun enableMapLocation() {
        if (checkPermission()) {
            googleMap?.isMyLocationEnabled = true
            locationViewModel.location.observe(viewLifecycleOwner, Observer { handleLocation(it) })
        }
    }

    private fun moveCameraLocation(latitude: Double, longitude: Double) {
        val camera = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 14.0f)
        googleMap?.animateCamera(camera, 300, null)
    }

    private fun showItemInfo(poi: POIViewData): Boolean {
        popupItem.txtName.text = poi.name
        popupItem.txtCategory.text = poi.category
        popupItem.txtDistance.text = "200m"

        popupItem.txtRating.visible = poi.rating != null
        popupItem.txtRating.text = poi.rating.toString()
        popupItem.imgItem.visible = !poi.image.isNullOrBlank()

        if (!popupItem.visible)
            popupItem.displayCircularReveal()

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
                if (!listMarkers.containsKey(poi.id)) {
                    val markerOptions = makeMarker(poi)
                    listMarkers[poi.id] = markerOptions
                    val marker = map.addMarker(markerOptions)
                    listMarkersId[marker.id] = poi.id
                }
            }
        }
    }

    private fun makeMarker(item: POIViewData): MarkerOptions {
        return MarkerOptions()
            .position(LatLng(item.latitude, item.longitude))
            .title(item.name)
            .snippet(item.description)
    }

    private fun handleLocation(location: LocationViewData) {
        moveCameraLocation(location.latitude, location.longitude)
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
                enableMapLocation()
            } else {
                TODO()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}