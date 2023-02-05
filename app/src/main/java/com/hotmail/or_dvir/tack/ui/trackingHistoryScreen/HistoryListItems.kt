package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel

data class HistoryListItems(
    val windows: List<SleepWakeWindowModel>,
) {
    val allSleepWindows = windows.filter { it.sleepWake == SleepWake.SLEEP }
    val allWakeWindows = windows.filter { it.sleepWake == SleepWake.WAKE }

    val longestSleep = allSleepWindows.maxBy { it.duration }.elapsedTimeUserFriendly
    val shortestSleep = allSleepWindows.minBy { it.duration }.elapsedTimeUserFriendly

    val longestWake = allWakeWindows.maxBy { it.duration }.elapsedTimeUserFriendly
    val shortestWake = allWakeWindows.minBy { it.duration }.elapsedTimeUserFriendly
}
