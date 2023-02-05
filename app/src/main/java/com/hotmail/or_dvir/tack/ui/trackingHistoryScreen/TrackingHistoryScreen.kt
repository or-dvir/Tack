package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.hotmail.or_dvir.tack.millisToUserFriendlyTime
import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.collectLatest

class TrackingHistoryScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<TrackingHistoryViewModel>()

        // todo collect vthis flow as lifecycle aware.
        //  do i really need to? this is a COLD flow
        val hhh = viewModel.groupedWindowsFlow.collectAsState(initial = emptyMap())



        Text("history screen")
    }

    @Composable
    private fun SleepWakeWindowRow(
        window: SleepWakeWindowModel
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = MaterialTheme.colors.primaryVariant,
                    painter = painterResource(window.sleepWake.iconRes),
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            window.apply {
                Text("${startMillis.millisToUserFriendlyTime()} - ")
                Text(endMillis.millisToUserFriendlyTime())
                Text(" ($elapsedTimeUserFriendly)")
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun SleepWakeWindowRowPreview() {
        val startTime = System.currentTimeMillis()
        SleepWakeWindowRow(
            SleepWakeWindowModel(
                startMillis = startTime,
                endMillis = startTime + TimeUnit.MINUTES.toMillis(25),
                sleepWake = SleepWake.SLEEP
            )
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun TrackingHistoryScreenPreview() {
        Content()
    }
}
