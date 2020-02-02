package com.jordansilva.map4.domain.model

import java.util.*

data class Location(val latitude: Double, val longitude: Double, val data: Long = Date().time) {

    fun asString(): String = "$latitude,$longitude"

}