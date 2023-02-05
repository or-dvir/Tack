package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel

data class HistoryListItems(
    val windows: List<SleepWakeWindowModel>,
) {
    private val allSleepWindows = windows.filter { it.sleepWake == SleepWake.SLEEP }
    private val allWakeWindows = windows.filter { it.sleepWake == SleepWake.WAKE }

    val longestSleep = allSleepWindows.maxByOrNull { it.duration }?.elapsedTimeUserFriendly
    val shortestSleep = allSleepWindows.minByOrNull { it.duration }?.elapsedTimeUserFriendly

    val longestWake = allWakeWindows.maxByOrNull { it.duration }?.elapsedTimeUserFriendly
    val shortestWake = allWakeWindows.minByOrNull { it.duration }?.elapsedTimeUserFriendly
}
