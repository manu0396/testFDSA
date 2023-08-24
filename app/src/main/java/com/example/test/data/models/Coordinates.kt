package com.example.test.data.models

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String
)