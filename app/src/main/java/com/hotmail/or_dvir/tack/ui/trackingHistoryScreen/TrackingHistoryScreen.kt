package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.hotmail.or_dvir.tack.millisToUserFriendlyDate
import com.hotmail.or_dvir.tack.millisToUserFriendlyTime
import com.hotmail.or_dvir.tack.models.SleepWake
import com.hotmail.or_dvir.tack.models.SleepWakeWindowModel
import java.util.concurrent.TimeUnit

class TrackingHistoryScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val viewModel = getViewModel<TrackingHistoryViewModel>()

        // todo collect this flow as lifecycle aware.
        //  do i really need to? this is a COLD flow
        val windowsMap = viewModel.groupedWindowsFlow.collectAsState(initial = emptyList()).value
//        val windowsMap = viewModel.groupedWindowsFlow.collectAsState(initial = emptyMap()).value

        // todo export this to a separate composable???
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            windowsMap.forEach { historyItems ->
                stickyHeader {
                    // just use the first sleep/wake window for the header
                    SleepWakeWindowStickyHeader(historyItems.windows.first().startMillis)
                }

                itemsIndexed(historyItems.windows) { index, window ->
                    SleepWakeWindowRow(window)
                    if (index != historyItems.windows.lastIndex) {
                        Divider()
                    }
                }
            }

            // todo
            //  each group has summary (longest//shortest nap/wake
        }
    }

    @Composable
    private fun SleepWakeWindowStickyHeader(startMillis: Long) {
        Text(
            text = startMillis.millisToUserFriendlyDate(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondaryVariant)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }

    @Composable
    private fun SleepWakeWindowRow(window: SleepWakeWindowModel) {
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
