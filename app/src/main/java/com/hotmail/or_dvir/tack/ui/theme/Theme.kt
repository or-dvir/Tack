package com.hotmail.or_dvir.tack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val primaryVariant = Color(0xff0288d1)

private val DarkColorPalette = darkColors(
    primary = Color(0xff0287c3),
    primaryVariant = primaryVariant,
    secondary = Color(0xff5af158),
    secondaryVariant = Color(0xff48C046)
)

private val LightColorPalette = lightColors(
    primary = Color(0xff03a9f4),
    primaryVariant = primaryVariant,
    secondary = Color(0xff5af158),
    secondaryVariant = Color(0xff48C046)
)

@Composable
fun TackTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    //todo when the app starts, it uses the color in the xml file, and only then switches to this
    // how can i fix this?
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = primaryVariant)
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
