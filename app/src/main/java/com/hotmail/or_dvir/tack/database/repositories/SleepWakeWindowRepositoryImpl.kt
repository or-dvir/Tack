package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.database.daos.SleepWakeWindowDao
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel
import com.hotmail.or_dvir.tack.toEntity
import com.hotmail.or_dvir.tack.toModel
import com.hotmail.or_dvir.tack.toModels
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SleepWakeWindowRepositoryImpl @Inject constructor(
    private val dao: SleepWakeWindowDao,
    private val scopeThatShouldNotBeCancelled: CoroutineScope,
    private val dispatcher: CoroutineDispatcher,
) : SleepWakeWindowRepository {

    // todo for now assume all operations are successful
    override fun getAll(): Flow<List<SleepWakeWindowModel>> = dao.getAll().map { it.toModels() }

    override suspend fun loadWindowById(id: Int): SleepWakeWindowModel {
        return withContext(dispatcher) {
            dao.loadWindowById(id).toModel()
        }
    }

    override suspend fun insertAll(vararg windows: SleepWakeWindowModel): List<Long> {
        return shouldNotBeCancelled {
            dao.insertAll(windows.map { it.toEntity() })
        }
    }

    override suspend fun delete(windowId: Int) {
        return shouldNotBeCancelled {
            dao.delete(windowId)
        }
    }

    private suspend inline fun <T : Any> shouldNotBeCancelled(
        crossinline operation: suspend (coroutineScope: CoroutineScope) -> T
    ): T {
        return withContext(dispatcher) {
            scopeThatShouldNotBeCancelled.async {
                operation(this)
            }.await()
        }
    }
}
