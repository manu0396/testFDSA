package es.paytef.cepsastandalone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.test.data.local.models.DestinationData

@Database(
    entities = [DestinationData::class, SellData::class, RegistrationData::class, ShiftData::class],
    version = 11,
    exportSchema = true
)
@TypeConverters(CustomTypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun dao(): CepsaDAO

}