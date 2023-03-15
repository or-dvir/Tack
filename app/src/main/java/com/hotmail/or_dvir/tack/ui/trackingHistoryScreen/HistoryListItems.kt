package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import com.hotmail.or_dvir.tack.millisElapsedTimeUserFriendly
import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel

data class HistoryListItems(
    val windows: List<SleepWakeWindowModel>,
) {
    private val allSleepWindows = windows.filter { it.sleepWake == SleepWake.SLEEP }
    private val allWakeWindows = windows.filter { it.sleepWake == SleepWake.WAKE }

    val longestSleep = allSleepWindows.maxByOrNull { it.durationMillis }?.elapsedTimeUserFriendly
    val shortestSleep = allSleepWindows.minByOrNull { it.durationMillis }?.elapsedTimeUserFriendly
    val totalSleep = allSleepWindows.sumOf { it.durationMillis }.millisElapsedTimeUserFriendly()

    val longestWake = allWakeWindows.maxByOrNull { it.durationMillis }?.elapsedTimeUserFriendly
    val shortestWake = allWakeWindows.minByOrNull { it.durationMillis }?.elapsedTimeUserFriendly
    val totalWake = allWakeWindows.sumOf { it.durationMillis }.millisElapsedTimeUserFriendly()
}
