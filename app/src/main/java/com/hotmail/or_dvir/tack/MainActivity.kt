package com.hotmail.or_dvir.tack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.hotmail.or_dvir.tack.ui.activeTrackingScreen.ActiveTrackingScreen
import com.hotmail.or_dvir.tack.ui.theme.TackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //todo data to save in app
    //  was the tracked cycle day or night
    //  was the tracked cycle awake or sleep
    //  how long each cycle lasted
    // from Ari:
    //  Nap begin (date and time stamp)
    //  Nap end
    //  Nap total for each napping session
    //  Nap daily total
    //  Wake window begin
    //  Wake window end
    //  Wake window total for each session
    //  Wake window daily total
    //  Same for"night sleep"
    //  Daily shortest nap/Wake
    //  Daily longest nap/wake

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ActiveTrackingScreen()
                }
            }
        }
    }
}

