package es.paytef.cepsastandalone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.paytef.cepsastandalone.data.local.models.ProductData
import es.paytef.cepsastandalone.data.local.models.SellData
import es.paytef.cepsastandalone.data.local.models.RegistrationData
import es.paytef.cepsastandalone.data.local.models.ShiftData

@Database(
    entities = [ProductData::class, SellData::class,RegistrationData::class, ShiftData::class],
    version = 11,
    exportSchema = true
)
@TypeConverters(CustomTypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun dao(): CepsaDAO

}