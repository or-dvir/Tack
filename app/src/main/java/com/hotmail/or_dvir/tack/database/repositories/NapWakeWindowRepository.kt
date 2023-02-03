package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.models.NapWakeWindowModel
import kotlinx.coroutines.flow.Flow

interface NapWakeWindowRepository {
    fun getAll(): Flow<List<NapWakeWindowModel>>
    suspend fun loadWindowById(id: Int): NapWakeWindowModel
    suspend fun insertAll(vararg windows: NapWakeWindowModel): List<Long>
    suspend fun delete(windowId: Int)
}
