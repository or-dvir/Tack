package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.models.NapWakeWindowModel

interface NapWakeWindowRepository {
    suspend fun getAll(): List<NapWakeWindowModel>
    suspend fun loadWindowById(id: Int): NapWakeWindowModel
    suspend fun insertAll(vararg windows: NapWakeWindowModel)
    suspend fun delete(windowId: Int)
}
