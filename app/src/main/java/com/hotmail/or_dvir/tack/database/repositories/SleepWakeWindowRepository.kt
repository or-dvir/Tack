package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel
import kotlinx.coroutines.flow.Flow

interface SleepWakeWindowRepository {
    fun getAll(): Flow<List<SleepWakeWindowModel>>
    suspend fun loadWindowById(id: Int): SleepWakeWindowModel
    suspend fun insertAll(vararg windows: SleepWakeWindowModel): List<Long>
    suspend fun delete(windowId: Int)
}
