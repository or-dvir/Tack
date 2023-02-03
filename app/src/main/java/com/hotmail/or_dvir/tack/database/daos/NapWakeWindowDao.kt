package com.hotmail.or_dvir.tack.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity.Companion.COLUMN_ID
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface NapWakeWindowDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Flow<List<NapWakeWindowEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    suspend fun loadWindowById(id: Int): NapWakeWindowEntity

    @Insert
    suspend fun insertAll(windows: List<NapWakeWindowEntity>): List<Long>

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = :windowId")
    suspend fun delete(windowId: Int)
}
