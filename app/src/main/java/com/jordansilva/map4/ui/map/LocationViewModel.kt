package com.jordansilva.map4.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.jordansilva.map4.domain.usecase.execute
import com.jordansilva.map4.domain.usecase.location.GetLocationUseCase
import com.jordansilva.map4.ui.BaseViewModel
import com.jordansilva.map4.ui.model.LocationViewData
import kotlinx.coroutines.flow.map

class LocationViewModel(getLocationUseCase: GetLocationUseCase) : BaseViewModel() {

    val location: LiveData<LocationViewData> = getLocationUseCase
        .execute()
        .map { LocationViewData(it.latitude, it.longitude) }
        .asLiveData()


}

