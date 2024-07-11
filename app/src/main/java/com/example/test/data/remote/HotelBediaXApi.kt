package com.example.test.data.remote

import com.example.test.data.models.Destination
import retrofit2.http.GET


fun interface HotelBediaXApi {

    @GET("/api/destinations")
    suspend fun getAll(): List<Destination>
}