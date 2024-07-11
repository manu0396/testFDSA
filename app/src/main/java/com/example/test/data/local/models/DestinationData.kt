package com.example.test.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.test.BuildConfig.DB_NAME
import com.example.test.data.local.CustomTypeConverters
import com.example.test.data.models.TimestampDTO

@Entity(tableName = DB_NAME)
data class DestinationData(
    @PrimaryKey(autoGenerate = false) val Id: String,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "Description") val Description: String,
    @ColumnInfo(name = "CountryCode") val CountryCode: String,
    @ColumnInfo(name = "Type") val Type: String,
    @ColumnInfo(name = "LastModify")
    @TypeConverters(CustomTypeConverters::class) // Add this line
    val LastModify: TimestampDTO,

)