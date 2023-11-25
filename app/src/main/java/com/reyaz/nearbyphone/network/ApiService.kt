package com.reyaz.nearbyphone.network

import com.reyaz.nearbyphone.data.EventsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("2/venues")
    suspend fun getVenues(
        @Query("per_page") size: Int,
        @Query("page") page: Int,
    ): Response<EventsData>

    @GET("2/venues")
    suspend fun getVenuesByLocation(
        @Query("per_page") size: Int,
        @Query("page") page: Int,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("range") range: String,
    ): Response<EventsData>

}