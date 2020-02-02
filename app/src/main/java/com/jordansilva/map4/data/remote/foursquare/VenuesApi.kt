package com.jordansilva.map4.data.remote.foursquare

import com.jordansilva.map4.data.remote.foursquare.model.VenuesSearchResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface VenuesApi {

    @GET("venues/search")
    fun searchByCoordinates(@Query("ll") coordinates: String,
                            @Query("intent") intent: String = "checkin",
                            @Query("radius") radius: Int = 250, //default in meters
                            @Query("limit") limit: Int = 50,
                            @Query("categoryId") categories: String? = null): Deferred<FoursquareResponse<VenuesSearchResponse>>

    @GET("venues/search?intent=browse")
    fun searchByBoundingBox(@Query("sw") southWest: String,
                            @Query("ne") northEast: String,
                            @Query("limit") limit: Int = 50,
                            @Query("query") query: String? = null,
                            @Query("categoryId") categories: String? = null): Deferred<FoursquareResponse<VenuesSearchResponse>>


//    @GET("venues/{id}")
//    fun getById(@Path ("id") id: String) : Deferred<FoursquareResponse<VenueDetailsResponse>>
//
//    @GET("venues/{id}/photos")
//    fun getPhotos(@Path("id") id: String) : Deferred<FoursquareResponse<PhotosResponse>>

}



