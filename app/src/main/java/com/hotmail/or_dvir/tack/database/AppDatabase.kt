package com.hotmail.or_dvir.tack.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hotmail.or_dvir.tack.database.daos.SleepWakeWindowDao
import com.hotmail.or_dvir.tack.database.entities.SleepWakeWindowEntity

@Database(entities = [SleepWakeWindowEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sleepWakeWindowDao(): SleepWakeWindowDao
}
