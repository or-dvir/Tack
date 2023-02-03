package com.hotmail.or_dvir.tack.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hotmail.or_dvir.tack.database.daos.NapWakeWindowDao
import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity

@Database(entities = [NapWakeWindowEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun napWakeWindowDao(): NapWakeWindowDao
}
