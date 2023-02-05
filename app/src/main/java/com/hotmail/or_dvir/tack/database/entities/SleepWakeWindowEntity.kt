package com.hotmail.or_dvir.tack.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hotmail.or_dvir.tack.models.SleepWake

@Entity(tableName = SleepWakeWindowEntity.TABLE_NAME)
data class SleepWakeWindowEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = COLUMN_START_MILLIS)
    val startMillis: Long,
    @ColumnInfo(name = "endMillis")
    val endMillis: Long,
    @ColumnInfo(name = "sleepWake")
    val sleepWake: SleepWake,
) {
    companion object {
        const val TABLE_NAME = "SleepWakeWindows"
        const val COLUMN_ID = "id"
        const val COLUMN_START_MILLIS = "startMillis"
    }
}
