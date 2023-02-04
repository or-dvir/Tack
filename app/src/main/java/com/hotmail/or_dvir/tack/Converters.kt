package com.hotmail.or_dvir.tack

import com.hotmail.or_dvir.tack.database.entities.SleepWakeWindowEntity
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel


fun List<SleepWakeWindowModel>.toEntities() = this.map { it.toEntity() }
fun SleepWakeWindowModel.toEntity() = SleepWakeWindowEntity(
    id = id,
    startMillis = startMillis,
    endMillis = endMillis,
    sleepWake = sleepWake
)

fun List<SleepWakeWindowEntity>.toModels() = this.map { it.toModel() }
fun SleepWakeWindowEntity.toModel() = SleepWakeWindowModel(
    id = id,
    startMillis = startMillis,
    endMillis = endMillis,
    sleepWake = sleepWake
)
