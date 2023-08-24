package com.example.test.data.models

import com.google.gson.annotations.SerializedName

data class Registered(
    @SerializedName("age") val age: Int,
    @SerializedName("date") val date: String
)