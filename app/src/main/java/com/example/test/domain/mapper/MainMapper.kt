package com.example.test.domain.mapper

import android.icu.util.Calendar
import androidx.core.graphics.set
import com.example.test.data.local.models.DestinationData
import com.example.test.data.models.Destination
import com.example.test.data.models.DestinationType
import com.example.test.data.models.Timestamp
import com.example.test.data.models.TimestampDTO
import com.example.test.domain.models.DestinationDomain

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
    fun destinationToDestionDomain(destination: Destination): DestinationDomain {
        return DestinationDomain(
            id = destination.Id.toString(),
            name = destination.Name,
            description = destination.Description,
            countryMode = destination.CountryCode,
            type = destination.Type.toString(),
        )
    }

    fun destinationDomainToDestination(destinationDomain: DestinationDomain): Destination {
        return Destination(
            Id = destinationDomain.id?.toInt() ?: 0,
            Name = destinationDomain.name ?: "",
            Description = destinationDomain.description ?: "",
            CountryCode = destinationDomain.countryMode ?: "",
            Type = DestinationType.valueOf(destinationDomain.type ?: ""),
            LastModify = TimestampDTO()
        )
    }

    fun destinationDataToDestionDomain(destinationData: DestinationData): DestinationDomain {
        return DestinationDomain(
            id = destinationData.Id,
            name = destinationData.Name,
            description = destinationData.Description,
            countryMode = destinationData.CountryCode,
            type = destinationData.Type,
            lastModify = destinationData.LastModify.toTimestamp()
        )
    }


    fun TimestampDTO.toTimestamp(): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, second)
        calendar.set(Calendar.MILLISECOND, millisecond)
        return Timestamp(calendar.timeInMillis)
    }
}