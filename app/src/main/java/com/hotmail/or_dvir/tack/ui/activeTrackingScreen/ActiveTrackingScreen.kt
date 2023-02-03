package com.hotmail.or_dvir.tack.ui.activeTrackingScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hotmail.or_dvir.tack.R
import com.hotmail.or_dvir.tack.collectAsStateLifecycleAware

private typealias OnUserAction = (UserAction) -> Unit

@Composable
fun ActiveTrackingScreen(
    viewModel: ActiveTrackingViewModel = hiltViewModel()
) {
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
private fun ColumnScope.Timer(
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
        horizontalArrangement = Arrangement.End
    ) {
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
        //todo make this with better button!!!

        Text(text = stringResource(R.string.sleep))
        Spacer(Modifier.width(5.dp))
        Switch(
            enabled = enabled,
            checked = !isSleep,
            onCheckedChange = { onUserAction(UserAction.SleepWakeButtonClick) }
        )
        Spacer(Modifier.width(5.dp))
        Text(text = stringResource(R.string.wake))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ActiveTrackingScreen()
}
