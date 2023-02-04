package com.hotmail.or_dvir.tack.models

enum class SleepWake {
    SLEEP, WAKE;

    fun reverse(): SleepWake = if (this == SLEEP) WAKE else SLEEP
}
