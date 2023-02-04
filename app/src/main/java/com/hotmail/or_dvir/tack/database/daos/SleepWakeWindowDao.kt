package com.hotmail.or_dvir.tack.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hotmail.or_dvir.tack.database.entities.SleepWakeWindowEntity
import com.hotmail.or_dvir.tack.database.entities.SleepWakeWindowEntity.Companion.COLUMN_ID
import com.hotmail.or_dvir.tack.database.entities.SleepWakeWindowEntity.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepWakeWindowDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<SleepWakeWindowEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    suspend fun loadWindowById(id: Int): SleepWakeWindowEntity

    @Insert
    suspend fun insertAll(windows: List<SleepWakeWindowEntity>): List<Long>

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = :windowId")
    suspend fun delete(windowId: Int)
}
