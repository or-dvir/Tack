package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import androidx.lifecycle.ViewModel
import com.hotmail.or_dvir.tack.database.repositories.SleepWakeWindowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class TrackingHistoryViewModel @Inject constructor(
    repo: SleepWakeWindowRepository,
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
            entry.value.map {
                it.first
            }
        }
    }
}
