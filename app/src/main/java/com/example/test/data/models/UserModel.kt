package com.example.test.data.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Result>
)