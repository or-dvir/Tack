package com.hotmail.or_dvir.tack.models

import androidx.annotation.DrawableRes
import com.hotmail.or_dvir.tack.R

enum class SleepWake(
    @DrawableRes
    val iconRes: Int,
) {
    SLEEP(R.drawable.ic_night),
    WAKE(R.drawable.ic_day);

    fun reverse(): SleepWake = if (this == SLEEP) WAKE else SLEEP
}
