package com.hotmail.or_dvir.tack.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = NapWakeWindowEntity.TABLE_NAME)
data class NapWakeWindowEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = "startMillis")
    val startMillis: Long,
    @ColumnInfo(name = "endMillis")
    val endMillis: Long
) {
    companion object {
        const val TABLE_NAME = "NapWakeWindows"
        const val COLUMN_ID = "id"
    }
}
