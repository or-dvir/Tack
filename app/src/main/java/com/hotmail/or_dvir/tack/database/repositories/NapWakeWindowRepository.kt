package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.models.NapWakeWindowModel

interface NapWakeWindowRepository {
    fun getAll(): List<NapWakeWindowModel>
    fun loadWindowById(id: Int): NapWakeWindowModel
    fun insertAll(vararg windows: NapWakeWindowModel)
    fun delete(windowId: Int)
}
