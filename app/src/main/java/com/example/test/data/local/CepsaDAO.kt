package es.paytef.cepsastandalone.data.local

import androidx.room.Dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.paytef.cepsastandalone.BuildConfig.DB_PRODUCTS_NAME
import es.paytef.cepsastandalone.BuildConfig.DB_SELLS_NAME
import es.paytef.cepsastandalone.BuildConfig.DB_SESSION_NAME
import es.paytef.cepsastandalone.BuildConfig.DB_SHIFT_NAME
import es.paytef.cepsastandalone.data.local.models.ProductData
import es.paytef.cepsastandalone.data.local.models.SellData
import es.paytef.cepsastandalone.data.local.models.RegistrationData
import es.paytef.cepsastandalone.data.local.models.ShiftData

@Dao
interface CepsaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShiftData(shiftData: ShiftData):Long

    @Query("SELECT * FROM $DB_SHIFT_NAME")
    fun getOpenShift(): ShiftData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTcod(registrationData: RegistrationData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productData: ProductData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSell(sellData: SellData): Long

    @Query("SELECT * FROM $DB_PRODUCTS_NAME")
    fun getProductData(): List<ProductData>

    @Query("DELETE FROM $DB_PRODUCTS_NAME WHERE name=:name")
    fun deleteProduct(name: String): Int

    @Query("SELECT tcod FROM $DB_SESSION_NAME")
    fun getTcod(): String?

    @Query("SELECT * FROM $DB_SESSION_NAME WHERE id=1")
    fun getSessionData(): RegistrationData?

    @Query("DELETE FROM $DB_SESSION_NAME WHERE sessionToken=:token")
    fun deleteSessionId(token: String): Int

    @Query("SELECT * FROM $DB_SELLS_NAME")
    fun getLocalSells(): List<SellData>
}
