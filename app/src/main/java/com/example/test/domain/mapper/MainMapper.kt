package com.example.test.domain.mapper

import com.example.test.data.local.models.DestinationData
import com.example.test.data.models.Destination
import com.example.test.data.models.DestinationType
import com.example.test.data.models.TimestampDTO

object MainMapper {
    fun destinationToDestinationData(destination: Destination): DestinationData {
        return DestinationData(
            Id = destination.Id.toString(),
            Name = destination.Name,
            Description = destination.Description,
            CountryCode = destination.CountryCode,
            Type = destination.Type.toString(),
            LastModify = TimestampDTO.parse(destination.LastModify.toString(), "dd/mm/yyyy HH:mm:ss")
        )
    }

    fun destinationDataToDestination(destinationData: DestinationData): Destination {
        return Destination(
            Id = destinationData.Id.toInt(),
            Name = destinationData.Name,
            Description = destinationData.Description,
            CountryCode = destinationData.CountryCode,
            Type = DestinationType.valueOf(destinationData.Type),
            LastModify = destinationData.LastModify
        )
    }
}