package com.hotmail.or_dvir.tack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.abs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

private const val ONE_SECOND_MILLIS = 1000L
private const val ONE_MINUTE_MILLIS = ONE_SECOND_MILLIS * 60
private const val ONE_HOUR_MILLIS = ONE_MINUTE_MILLIS * 60

const val MAX_SEC_MIN = 59
const val SINGLE_DIGIT_LIMIT = 10

/**
 * @return Triple
 *  * first - hours
 *  * second - minutes
 *  * third - seconds
 */
fun timeElapsed(startMillis: Long, endMillis: Long): Triple<Int, Int, Int> {
    // todo for now, no input validation

    var different = abs(endMillis - startMillis)

    val elapsedHours = different / ONE_HOUR_MILLIS
    different %= ONE_HOUR_MILLIS

    val elapsedMinutes = different / ONE_MINUTE_MILLIS
    different %= ONE_MINUTE_MILLIS

    val elapsedSeconds = different / ONE_SECOND_MILLIS

    return Triple(elapsedHours.toInt(), elapsedMinutes.toInt(), elapsedSeconds.toInt())
}

fun Long.millisToUserFriendlyDate(): String {
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
    return dateFormat.format(Date(this@millisToUserFriendlyDate))
}

fun Long.millisToUserFriendlyTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Date(this@millisToUserFriendlyTime))
}

//region copied from https://proandroiddev.com/how-to-collect-flows-lifecycle-aware-in-jetpack-compose-babd53582d0b
@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(
//fun <T> StateFlow<T>.collectAsStateLifecycleAware(
    context: CoroutineContext = EmptyCoroutineContext
//    context: CoroutineContext = rememberCoroutineScope().coroutineContext
): State<T> = collectAsStateLifecycleAware(initial = value, context = context)

@Composable
private fun <T : R, R> Flow<T>.collectAsStateLifecycleAware(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext
): State<R> {
    val lifecycleAwareFlow = rememberFlow(flow = this)
    return lifecycleAwareFlow.collectAsState(initial = initial, context = context)
}

@Composable
private fun <T> rememberFlow(
    flow: Flow<T>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
): Flow<T> {
    return remember(
        key1 = flow,
        key2 = lifecycleOwner
    ) { flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED) }
}
//endregion