package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.tack.database.repositories.SleepWakeWindowRepository
import com.hotmail.or_dvir.tack.millisToUserFriendlyDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class TrackingHistoryViewModel @Inject constructor(
    private val repo: SleepWakeWindowRepository,
) : ViewModel() {

    val groupedWindowsFlow = repo.getAllSortedByStartDescending().map { allWindows ->

        // todo is this part too inefficient??? how is the performance??
        allWindows.map {
            it to Calendar.getInstance().apply { timeInMillis = it.startMillis }
        }.groupBy {
            Triple(
                it.second.get(Calendar.YEAR),
                it.second.get(Calendar.MONTH),
                it.second.get(Calendar.DAY_OF_MONTH)
            )
        }.mapValues { entry ->
            entry.value.map { it.first }
        }
    }

//    init {
//        viewModelScope.launch {
//            groupedWindowsFlow.collectLatest { map ->
//                map.forEach {
//                    val ttt =
//                        "${it.key} -> ${it.value.joinToString(separator = " ;;; ") { cal -> cal.timeInMillis.millisToUserFriendlyDate() }}"
//                    Log.i("aaaaa", ttt)
//                }
//            }
//        }
//    }
}
