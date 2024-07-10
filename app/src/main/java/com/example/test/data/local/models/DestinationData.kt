package com.example.test.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import es.paytef.cepsastandalone.BuildConfig.DB_PRODUCTS_NAME

@Entity(tableName = DB_DESTINATIONS_NAME)
data class DestinationData(
    @PrimaryKey(autoGenerate = false) val id: String,

)