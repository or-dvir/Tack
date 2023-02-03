package com.hotmail.or_dvir.tack.database.repositories

import com.hotmail.or_dvir.tack.database.daos.NapWakeWindowDao
import com.hotmail.or_dvir.tack.models.NapWakeWindowModel
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

class NapWakeWindowRepositoryImpl @Inject constructor(
    private val dao: NapWakeWindowDao,
    private val scopeThatShouldNotBeCancelled: CoroutineScope,
    private val dispatcher: CoroutineDispatcher,
) : NapWakeWindowRepository {

    // todo for now assume all operations are successful
    override fun getAll(): Flow<List<NapWakeWindowModel>> = dao.getAll().map { it.toModels() }

    override suspend fun loadWindowById(id: Int): NapWakeWindowModel {
        return withContext(dispatcher) {
            dao.loadWindowById(id).toModel()
        }
    }

    override suspend fun insertAll(vararg windows: NapWakeWindowModel): List<Long> {
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
