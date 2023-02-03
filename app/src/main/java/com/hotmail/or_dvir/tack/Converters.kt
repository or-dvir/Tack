package com.hotmail.or_dvir.tack

import com.hotmail.or_dvir.tack.database.entities.NapWakeWindowEntity
import com.hotmail.or_dvir.tack.models.NapWakeWindowModel


fun List<NapWakeWindowModel>.toEntities() = this.map { it.toEntity() }
fun NapWakeWindowModel.toEntity() = NapWakeWindowEntity(
    id = id,
    startMillis = startMillis,
    endMillis = endMillis
)

fun List<NapWakeWindowEntity>.toModels() = this.map { it.toModel() }
fun NapWakeWindowEntity.toModel() = NapWakeWindowModel(
    id = id,
    startMillis = startMillis,
    endMillis = endMillis
)
