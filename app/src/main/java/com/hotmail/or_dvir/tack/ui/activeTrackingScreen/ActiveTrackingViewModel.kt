package com.hotmail.or_dvir.tack.ui.activeTrackingScreen

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.tack.database.repositories.SleepWakeWindowRepository
import com.hotmail.or_dvir.tack.models.Chronometer
import com.hotmail.or_dvir.tack.models.DayNight
import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel
import com.hotmail.or_dvir.tack.timeElapsed
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveTrackingViewModel @Inject constructor(
    private val repo: SleepWakeWindowRepository,
    context: Application
) : AndroidViewModel(context) {

    companion object {
        private const val DEFAULT_START_TIME = -1L
        private const val SHARED_PREFS_NAME = "SHARED_PREFS_START_TIME"
        private const val PREFS_KEY_START_TIME = "PREFS_KEY_START_TIME"
    }

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    private val chronometer = Chronometer()

    private val timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            chronometer.tick()
            state.apply {
                updateState(
                    newState = value.copy(
                        hours = chronometer.hoursStr,
                        minutes = chronometer.minutesStr,
                        seconds = chronometer.secondsStr
                    )
                )
            }
        }

        override fun onFinish() {
            // should not trigger as we have a very high starting value
        }
    }

    init {
        var startTime = readSleepWakeWindowStartMillis()

        if (startTime == DEFAULT_START_TIME) {
            startTime = System.currentTimeMillis()
            writeSleepWakeWindowStartMillis(startTime)
        }

        timeElapsed(startTime, System.currentTimeMillis()).let {
            chronometer.apply {
                hours = it.first
                minutes = it.second
                seconds = it.third
            }
        }

        timer.start()
    }

    private fun readSleepWakeWindowStartMillis() =
        sharedPrefs.getLong(PREFS_KEY_START_TIME, DEFAULT_START_TIME)

    private fun writeSleepWakeWindowStartMillis(millis: Long = System.currentTimeMillis()) {
        sharedPrefs.edit {
            putLong(PREFS_KEY_START_TIME, millis)
        }
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }

    private val _state = MutableStateFlow(ActiveTrackingState())
    val state = _state.asStateFlow()

    fun handleUserAction(action: UserAction) {
        when (action) {
            UserAction.DayNightButtonClick -> onDayNightButtonClicked()
            UserAction.SleepWakeButtonClick -> onSleepWakeButtonClicked()
        }
    }

    private fun insertCurrentStateToDb() {
        val start = readSleepWakeWindowStartMillis()
        val end = System.currentTimeMillis()

        // we don't log anything less than 5 minutes
        if ((end - start) < TimeUnit.MINUTES.toMillis(5)) {
            return
        }

        viewModelScope.launch {
            repo.insertAll(
                SleepWakeWindowModel(
                    startMillis = start,
                    endMillis = end,
                    sleepWake = state.value.sleepWake
                )
            )
        }
    }

    private fun onSleepWakeButtonClicked() {
        insertCurrentStateToDb()

        // starting a new sleep/wake window. resetting start time
        writeSleepWakeWindowStartMillis()
        chronometer.reset()
        state.value.apply {
            updateStateWithDefaultTimer(
                newState = copy(
                    sleepWake = sleepWake.reverse()
                )
            )
        }
    }

    private fun onDayNightButtonClicked() {
        state.value.apply {
            insertCurrentStateToDb()

            val newDayNight = dayNight.reverse()
            // we stop tracking at night
            if (newDayNight == DayNight.NIGHT) {
                writeSleepWakeWindowStartMillis(DEFAULT_START_TIME)
                chronometer.reset()
                timer.cancel()
            } else {
                writeSleepWakeWindowStartMillis()
                timer.start()
            }

            updateStateWithDefaultTimer(
                newState = copy(
                    dayNight = newDayNight
                )
            )
        }
    }

    private fun updateStateWithDefaultTimer(newState: ActiveTrackingState) {
        updateState(
            newState.copy(
                hours = ActiveTrackingState.DEFAULT_HRS_MIN_SEC,
                minutes = ActiveTrackingState.DEFAULT_HRS_MIN_SEC,
                seconds = ActiveTrackingState.DEFAULT_HRS_MIN_SEC,
            )
        )
    }

    private fun updateState(newState: ActiveTrackingState) {
        viewModelScope.launch {
            _state.emit(newState)
        }
    }
}

sealed class UserAction {
    object DayNightButtonClick : UserAction()
    object SleepWakeButtonClick : UserAction()
}

data class ActiveTrackingState(
    val dayNight: DayNight = DayNight.DAY,
    val sleepWake: SleepWake = SleepWake.WAKE,
    val hours: String = DEFAULT_HRS_MIN_SEC,
    val minutes: String = DEFAULT_HRS_MIN_SEC,
    val seconds: String = DEFAULT_HRS_MIN_SEC,
) {
    companion object {
        const val DEFAULT_HRS_MIN_SEC = "00"
    }
}
