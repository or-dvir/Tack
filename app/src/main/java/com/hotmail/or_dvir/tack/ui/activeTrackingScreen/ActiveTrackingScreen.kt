package com.hotmail.or_dvir.tack.ui.activeTrackingScreen

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hotmail.or_dvir.tack.R
import com.hotmail.or_dvir.tack.collectAsStateLifecycleAware
import com.hotmail.or_dvir.tack.models.DayNight
import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.ui.trackingHistoryScreen.TrackingHistoryScreen

private typealias OnUserAction = (UserAction) -> Unit

class ActiveTrackingScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<ActiveTrackingViewModel>()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            viewModel.apply {
                val collectedState = viewModel.state.collectAsStateLifecycleAware().value

                DayNightControls(
                    state = collectedState,
                    onUserAction = viewModel::handleUserAction
                )

                Timer(
                    hours = collectedState.hours,
                    minutes = collectedState.minutes,
                    seconds = collectedState.seconds
                )

                SleepWakeControls(
                    enabled = collectedState.dayNight != DayNight.NIGHT,
                    isSleep = collectedState.sleepWake == SleepWake.SLEEP,
                    onUserAction = viewModel::handleUserAction
                )
            }
        }
    }

    @Composable
    private fun Timer(
        hours: String,
        minutes: String,
        seconds: String
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(2.dp, MaterialTheme.colors.primary, CircleShape)
        ) {
            // todo make text resize to fit
            Text(
                maxLines = 1,
                style = MaterialTheme.typography.h1,
                text = "$hours:$minutes:$seconds"
            )
        }
    }

    @Composable
    private fun DayNightControls(
        state: ActiveTrackingState,
        onUserAction: OnUserAction
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navigator = LocalNavigator.currentOrThrow
            IconButton(
                onClick = { navigator.push(TrackingHistoryScreen()) }
            ) {
                Icon(
                    tint = MaterialTheme.colors.primaryVariant,
                    imageVector = Icons.Filled.List,
                    contentDescription = stringResource(R.string.contentDescription_trackingHistory)
                )
            }

            FloatingActionButton(
                onClick = { onUserAction(UserAction.DayNightButtonClick) }
            ) {
                // the day/night button describes what will happen when you click the button,
                // so it shows the opposite of the current state
                state.dayNight.apply {
                    Icon(
                        painter = painterResource(reverseIconRes()),
                        contentDescription = stringResource(reverseContentDescriptionRes())
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun SleepWakeControls(
        enabled: Boolean,
        isSleep: Boolean,
        onUserAction: OnUserAction
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // sleep chip
            ToggleChip(
                startChip = true,
                enabled = enabled,
                selected = isSleep,
                textRes = R.string.sleep,
                onClick = { onUserAction(UserAction.SleepWakeButtonClick) }
            )

            // wake chip
            ToggleChip(
                startChip = false,
                enabled = enabled,
                selected = !isSleep,
                textRes = R.string.wake,
                onClick = { onUserAction(UserAction.SleepWakeButtonClick) }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ToggleChip(
        startChip: Boolean,
        enabled: Boolean,
        selected: Boolean,
        @StringRes textRes: Int,
        onClick: () -> Unit
    ) {
        val outerCornerSize = remember { CornerSize(percent = 50) }
        val innerCornerSize = remember { ZeroCornerSize }

        FilterChip(
            colors = ChipDefaults.filterChipColors(
                selectedBackgroundColor = MaterialTheme.colors.secondary
            ),
            shape = MaterialTheme.shapes.small.copy(
                topStart = if (startChip) outerCornerSize else innerCornerSize,
                bottomStart = if (startChip) outerCornerSize else innerCornerSize,
                topEnd = if (startChip) innerCornerSize else outerCornerSize,
                bottomEnd = if (startChip) innerCornerSize else outerCornerSize,
            ),
            enabled = enabled,
            selected = selected,
            onClick = {
                // only perform click if not already selected
                if (!selected) {
                    onClick()
                }
            }
        ) {
            Text(text = stringResource(textRes))
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun HomeScreenPreview() {
        Content()
    }
}
