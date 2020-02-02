package com.jordansilva.map4.ui.mapper

interface ViewDataMapper<Domain, ViewData> {
    fun toViewData(model: Domain): ViewData
}