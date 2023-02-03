package com.hotmail.or_dvir.tack.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity.Companion.COLUMN_ID
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity.Companion.TABLE_NAME

@Dao
interface NapWakeWindowDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<NapWakeWindowEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun loadWindowById(id: Int): NapWakeWindowEntity

    @Insert
    fun insertAll(windows: List<NapWakeWindowEntity>)

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = :windowId")
    fun delete(windowId: Int)
}
