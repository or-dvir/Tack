package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.database.daos.NapWakeWindowDao
import com.hotmail.or_dvir.tack.models.NapWakeWindowModel
import com.hotmail.or_dvir.tack.toEntity
import com.hotmail.or_dvir.tack.toModel
import javax.inject.Inject

class NapWakeWindowRepositoryImpl @Inject constructor(
    private val dao: NapWakeWindowDao
) : NapWakeWindowRepository {

    // todo
    //  make whatever needs to be non-cancellable
    //  make everything here run in coroutine

    override suspend fun getAll(): List<NapWakeWindowModel> = dao.getAll().map { it.toModel() }

    override suspend fun loadWindowById(id: Int) = dao.loadWindowById(id).toModel()

    override suspend fun insertAll(vararg windows: NapWakeWindowModel) =
        dao.insertAll(windows.map { it.toEntity() })

    override suspend fun delete(windowId: Int) = dao.delete(windowId)
}
