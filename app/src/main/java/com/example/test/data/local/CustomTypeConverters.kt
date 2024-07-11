package com.example.test.data.local

import androidx.room.TypeConverter
import com.example.test.data.models.Timestamp
import com.example.test.data.models.TimestampDTO
import com.google.gson.Gson

class CustomTypeConverters {
    @TypeConverter
    fun fromTimestampToJson(timestamp: Timestamp?):String{
        return Gson().toJson(timestamp)
    }
    @TypeConverter
    fun fromJsonToTimestamp(json:String):Timestamp{
        return Gson().fromJson(json, Timestamp::class.java)
    }

    @TypeConverter
    fun fromTimestampToLong(timestamp: Timestamp?): Long? {
        return timestamp?.millis
    }

    @TypeConverter
    fun fromLongToTimestamp(millis: Long?): Timestamp? {
        return millis?.let { Timestamp(it) }
    }

    @TypeConverter
    fun fromTimestampDtoToJson(timestampDto: TimestampDTO?): String {
        return Gson().toJson(timestampDto)
    }

    @TypeConverter
    fun fromJsonToTimestampDto(json: String): TimestampDTO {
        return Gson().fromJson(json, TimestampDTO::class.java)
    }
}