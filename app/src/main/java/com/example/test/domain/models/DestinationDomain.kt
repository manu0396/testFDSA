package com.example.test.domain.models

import com.example.test.data.models.Timestamp

data class DestinationDomain(
    val id: String? = null,
    var name:String? = null,
    val description:String? = null,
    val countryMode:String? = null,
    val type: String? = null,
    val picture:String? = null,
    val lastModify:Timestamp? = null
)