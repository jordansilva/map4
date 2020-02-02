package com.jordansilva.map4.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jordansilva.map4.domain.model.POI
import com.jordansilva.map4.domain.usecase.poi.GetPlacesByAreaRequest
import com.jordansilva.map4.domain.usecase.poi.GetPlacesByAreaUseCase
import com.jordansilva.map4.ui.BaseViewModel
import com.jordansilva.map4.ui.mapper.POIViewDataMapper
import com.jordansilva.map4.ui.model.POIViewData
import com.jordansilva.map4.util.ErrorResult
import com.jordansilva.map4.util.onFailure
import com.jordansilva.map4.util.onSuccess

class POIViewModel(private val getNearbyPlacesUseCase: GetPlacesByAreaUseCase) : BaseViewModel() {

    private val _poiList = MutableLiveData<List<POIViewData>>()
    val poiList: LiveData<List<POIViewData>> = _poiList

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun getNearbyPlaces(latitudeSW: Double, longitudeSW: Double, latitudeNE: Double, longitudeNE: Double) {
        _loading.value = true

        launch {
            val request = GetPlacesByAreaRequest(
                latitudeSW = latitudeSW,
                longitudeSW = longitudeSW,
                latitudeNE = latitudeNE,
                longitudeNE = longitudeNE
            )
            getNearbyPlacesUseCase
                .execute(request)
                .onSuccess(::handleSuccess)
                .onFailure(::handleFailure)
        }.invokeOnCompletion { _loading.postValue(false) }
    }

    private fun handleFailure(errorResult: ErrorResult) {

    }

    private fun handleSuccess(data: List<POI>) {
        val pois = data.map { POIViewDataMapper.toViewData(it) }
        _poiList.postValue(pois)
    }

}