package com.example.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.test.data.local.CustomTypeConverters
import com.example.test.data.local.HotelDAO
import com.example.test.data.local.models.DestinationData

@Database(
    entities = [DestinationData::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(CustomTypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun dao(): HotelDAO

}