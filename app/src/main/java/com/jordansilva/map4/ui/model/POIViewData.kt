package com.jordansilva.map4.ui.model

data class POIViewData(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null,
    val category: String? = null,
    val image: String? = null,
    val rating: Double? = null
) {

    fun getRating(): String = rating.toString()

}