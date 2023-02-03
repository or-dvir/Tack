package com.hotmail.or_dvir.tack.models

data class NapWakeWindowModel(
    // generated by room
    val id: Int,
    val startMillis: Long,
    val endMillis: Long
) {
    val totalTimeMillis = endMillis - startMillis
}
