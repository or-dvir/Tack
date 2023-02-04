package com.hotmail.or_dvir.tack.ui.activeTrackingScreen

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.tack.R
import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepository
import com.hotmail.or_dvir.tack.models.Chronometer
import com.hotmail.or_dvir.tack.models.NapWakeWindowModel
import com.hotmail.or_dvir.tack.timeElapsed
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

private const val DATA_STORE_NAME = "TackDataStore"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

@HiltViewModel
class ActiveTrackingViewModel @Inject constructor(
    private val repo: NapWakeWindowRepository,
    private val context: Application
) : AndroidViewModel(context) {

    companion object {
        private const val DEFAULT_START_TIME = -1L
        private const val KEY_NAME_NAP_WAKE_START = "NAP_WAKE_START"
        private val STORE_KEY_NAP_WAKE_START = longPreferencesKey(KEY_NAME_NAP_WAKE_START)
    }

    private val chronometer = Chronometer()

    private val timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.i("aaaaa", "tick")
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
        Log.i("aaaaa", "one")
        viewModelScope.launch {
            var startTime = readNapWakeWindowStartMillis()
            Log.i("aaaaa", "two")

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

            Log.i("aaaaa", "three")
            timer.start()
        }
    }

    private suspend fun readNapWakeWindowStartMillis() =
        context.dataStore.data.lastOrNull()?.get(STORE_KEY_NAP_WAKE_START) ?: DEFAULT_START_TIME

    private fun writeNapWakeWindowStartMillis(millis: Long = System.currentTimeMillis()): Job {
        return viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[STORE_KEY_NAP_WAKE_START] = millis
            }
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
