package com.hotmail.or_dvir.tack.ui.activeTrackingScreen

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.tack.R
import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepository
import com.hotmail.or_dvir.tack.models.Chronometer
import com.hotmail.or_dvir.tack.models.NapWakeWindowModel
import com.hotmail.or_dvir.tack.timeElapsed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveTrackingViewModel @Inject constructor(
    private val repo: NapWakeWindowRepository,
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
        var startTime = readNapWakeWindowStartMillis()

        if (startTime == DEFAULT_START_TIME) {
            startTime = System.currentTimeMillis()
            writeNapWakeWindowStartMillis(startTime)
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

    private fun readNapWakeWindowStartMillis() =
        sharedPrefs.getLong(PREFS_KEY_START_TIME, DEFAULT_START_TIME)

    private fun writeNapWakeWindowStartMillis(millis: Long = System.currentTimeMillis()) {
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
            UserAction.NapWakeButtonClick -> onNapWakeButtonClicked()
        }
    }

    private fun onNapWakeButtonClicked() {
        viewModelScope.launch {
//            need to know whether its sleep / wake -add to model and database entity (and converters)
//
//            need to create the start time ... should be saved in shared preferences in case the app
//            dies.when this view model resumes, calculate the new state (hrs/min/sec) and emit to
//            ui

            val window = NapWakeWindowModel(
                startMillis = readNapWakeWindowStartMillis(),
                endMillis = System.currentTimeMillis(),
            )

            repo.insertAll(window)
        }

        // starting a new nap/wake window. resetting start time
        writeNapWakeWindowStartMillis()
        chronometer.reset()
        state.value.apply {
            updateStateWithDefaultTimer(
                newState = copy(
                    napWake = napWake.reverse()
                )
            )
        }
    }

    private fun onDayNightButtonClicked() {
        state.value.apply {
            val newDayNight = dayNight.reverse()

            // we stop tracking at night
            if (newDayNight == DayNight.NIGHT) {
                writeNapWakeWindowStartMillis(DEFAULT_START_TIME)
                chronometer.reset()
                timer.cancel()
            } else {
                writeNapWakeWindowStartMillis()
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
                hours = ActiveTrackingState.DEFAULT_HOURS,
                minutes = ActiveTrackingState.DEFAULT_MINUTES_SECONDS,
                seconds = ActiveTrackingState.DEFAULT_MINUTES_SECONDS,
            )
        )
    }

    private fun updateState(newState: ActiveTrackingState) {
        viewModelScope.launch {
            _state.emit(newState)
        }
    }
}

enum class DayNight(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val contentDescriptionRes: Int
) {
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

enum class NapWake {
    NAP, WAKE;

    fun reverse(): NapWake = if (this == NAP) WAKE else NAP
}

sealed class UserAction {
    object DayNightButtonClick : UserAction()
    object NapWakeButtonClick : UserAction()
}

data class ActiveTrackingState(
    val dayNight: DayNight = DayNight.DAY,
    val napWake: NapWake = NapWake.WAKE,
    val hours: String = DEFAULT_HOURS,
    val minutes: String = DEFAULT_MINUTES_SECONDS,
    val seconds: String = DEFAULT_MINUTES_SECONDS,
) {
    companion object {
        const val DEFAULT_MINUTES_SECONDS = "00"
        const val DEFAULT_HOURS = "0"
    }
}
