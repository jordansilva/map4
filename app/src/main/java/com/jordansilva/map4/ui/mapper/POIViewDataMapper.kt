package com.jordansilva.map4.ui.mapper

import com.jordansilva.map4.domain.model.POI
import com.jordansilva.map4.ui.model.POIViewData

object POIViewDataMapper : ViewDataMapper<POI, POIViewData> {
    override fun toViewData(model: POI): POIViewData {
        return POIViewData(
            id = model.id,
            name = model.name,
            description = model.description,
            latitude = model.latitude,
            longitude = model.longitude
        )
    }

}