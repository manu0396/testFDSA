package com.example.test.data.local

import androidx.room.TypeConverter
import com.example.test.data.models.Timestamp
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
}