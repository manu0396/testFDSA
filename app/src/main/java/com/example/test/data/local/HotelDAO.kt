package com.example.test.data.local

import androidx.room.Dao
import androidx.room.Delete

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.test.data.local.models.DestinationData

@Dao
interface HotelDAO {
   @Insert
   suspend fun insert(destinationData: DestinationData): Int

   @Query("SELECT * FROM destinations")
   suspend fun getAll(): List<DestinationData>

   @Delete
   suspend fun delete(destinationData: DestinationData): Int

   @Update
   suspend fun update(destinationData: DestinationData): Int

   @Query("SELECT * FROM destinations WHERE id = :id")
   suspend fun selectById(id: String)
}
