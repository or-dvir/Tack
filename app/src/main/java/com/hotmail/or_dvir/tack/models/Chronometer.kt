package com.hotmail.or_dvir.tack.models

class Chronometer {

    private companion object {
        const val MAX_SEC_MIN = 59
        const val SINGLE_DIGIT_LIMIT = 10
    }

    private var hours: Int = 0
    val hoursStr get() = hours.toString()
    private var minutes: Int = 0
    val minutesStr get() = minutes.takeUnless { it < SINGLE_DIGIT_LIMIT }?.toString() ?: "0$minutes"
    private var seconds: Int = 0
    val secondsStr get() = seconds.takeUnless { it < SINGLE_DIGIT_LIMIT }?.toString() ?: "0$seconds"

    fun reset() {
        hours  = 0
        minutes = 0
        seconds = 0
    }

    fun tick() {
        seconds++
        if (seconds > MAX_SEC_MIN) {
            tickMinutes()
            seconds = 0
        }
    }

    private fun tickMinutes() {
        minutes++
        if (minutes > MAX_SEC_MIN) {
            hours++
            minutes = 0
        }
    }
}
