package es.paytef.cepsastandalone.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import es.paytef.cepsastandalone.BuildConfig.DB_PRODUCTS_NAME
import es.paytef.cepsastandalone.utils.Constants

@Entity(tableName = DB_PRODUCTS_NAME)
data class ProductData(
    @PrimaryKey(autoGenerate = false) val id: String,

)