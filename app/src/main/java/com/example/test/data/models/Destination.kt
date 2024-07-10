package com.example.test.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Destination (
    @SerialName(value = "Id") var Id: Int,
    @SerialName(value = "Name") var Name: String,
    @SerialName(value = "Description") var Description: String,
    @SerialName(value = "CountryCode") var CountryCode: String,
    @SerialName(value = "Type") var Type: DestinationType,
    @SerialName(value = "LastModify") var LastModify: TimestampDTO
)