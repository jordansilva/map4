package com.jordansilva.map4.data.remote.foursquare

data class FoursquareResponse<T>(val meta: Meta, val response: T) {
    data class Meta(val code: Int,
                    val requestId: String,
                    val errorType: String,
                    val errorDetail: String)

    fun isSuccess() = meta.code == 200 && response != null
}