package com.hotmail.or_dvir.tack.models

import com.hotmail.or_dvir.tack.MAX_SEC_MIN
import com.hotmail.or_dvir.tack.SINGLE_DIGIT_LIMIT

class Chronometer {

    var hours: Int = 0
    val hoursStr get() = hours.takeUnless { it < SINGLE_DIGIT_LIMIT }?.toString() ?: "0$hours"

    var minutes: Int = 0
    val minutesStr get() = minutes.takeUnless { it < SINGLE_DIGIT_LIMIT }?.toString() ?: "0$minutes"

    var seconds: Int = 0
    val secondsStr get() = seconds.takeUnless { it < SINGLE_DIGIT_LIMIT }?.toString() ?: "0$seconds"

    fun reset() {
        hours = 0
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
