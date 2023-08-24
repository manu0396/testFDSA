package com.example.test.data

import com.example.test.data.models.UserModel
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiRandomUser {

    @Headers("Content-Type: application/json")
    @GET("/")
    suspend fun getResults():UserModel
}