package com.example.test.data.models

import kotlinx.serialization.Serializable

@Serializable
class Destination (
    val id: Int,
    val name: String,
    val description: String,
    val CountryCode: String,
    val Type: DestinationType,
    val LastModify: TimestampDTO
)