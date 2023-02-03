package com.hotmail.or_dvir.tack.ui.activeTrackingScreen

import android.os.CountDownTimer
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.tack.R
import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepository
import com.hotmail.or_dvir.tack.models.Chronometer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveTrackingViewModel @Inject constructor(
    private val repo: NapWakeWindowRepository
) : ViewModel() {

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
        timer.start()
        //todo set chronometer initial value according to previously saved one in data source
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

    private fun onSleepWakeButtonClicked() {
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
            val newDayNight = dayNight.reverse()

            // we stop tracking at night
            if (newDayNight == DayNight.NIGHT) {
                chronometer.reset()
                timer.cancel()
            } else {
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

enum class SleepWake {
    SLEEP, WAKE;

    fun reverse(): SleepWake = if (this == SLEEP) WAKE else SLEEP
}

sealed class UserAction {
    object DayNightButtonClick : UserAction()
    object SleepWakeButtonClick : UserAction()
}

data class ActiveTrackingState(
    val dayNight: DayNight = DayNight.DAY,
    val sleepWake: SleepWake = SleepWake.WAKE,
    val hours: String = DEFAULT_HOURS,
    val minutes: String = DEFAULT_MINUTES_SECONDS,
    val seconds: String = DEFAULT_MINUTES_SECONDS,
) {
    companion object {
        const val DEFAULT_MINUTES_SECONDS = "00"
        const val DEFAULT_HOURS = "0"
    }
}
