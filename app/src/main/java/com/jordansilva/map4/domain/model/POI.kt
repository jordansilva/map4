package com.jordansilva.map4.domain.model

data class POI(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String?,
    val category: String?,
    val image: String? = null,
    val rating: Double? = null
)

