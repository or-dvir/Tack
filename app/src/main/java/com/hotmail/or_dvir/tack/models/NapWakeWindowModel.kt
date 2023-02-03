package com.hotmail.or_dvir.tack.models

data class NapWakeWindowModel(
    val startMillis: Long,
    val endMillis: Long,
    // generated by room
    val id: Int = 0,
) {
    val totalTimeMillis = endMillis - startMillis
}
