package com.hotmail.or_dvir.tack.ui.trackingHistoryScreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen

class TrackingHistoryScreen: Screen {
    @Composable
    override fun Content() {
        Text("history screen")
    }

    @Preview(showBackground = true)
    @Composable
    private fun TrackingHistoryScreenPreview() {
        Content()
    }
}
