package com.hotmail.or_dvir.tack.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hotmail.or_dvir.tack.R

enum class DayNight(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val contentDescriptionRes: Int
) {
    //WARNING!!! WARNING!!! WARNING!!!
    // do NOT rename!!! used in shared preferences
    DAY(
        contentDescriptionRes = R.string.contentDescription_day,
        iconRes = R.drawable.ic_day
    ),
    NIGHT(
        contentDescriptionRes = R.string.contentDescription_night,
        iconRes = R.drawable.ic_night
    );

    @DrawableRes
    fun reverseIconRes() = reverse().iconRes

    @StringRes
    fun reverseContentDescriptionRes() = reverse().contentDescriptionRes
    fun reverse(): DayNight = if (this == NIGHT) DAY else NIGHT
}
