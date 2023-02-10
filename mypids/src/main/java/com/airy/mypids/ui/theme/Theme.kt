package com.airy.mypids.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airy.mypids.HomeUI

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = red_500,
    primaryVariant = red_500,
    secondary = golden_500,
    onPrimary = Color.Black,
    surface = light_gray_100,
    error = Color.Magenta
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val DarkColorPalette = darkColors(
    primary = red_500,
    primaryVariant = red_500,
    secondary = golden_500,
    surface = dark_gray_100,
    error = Color.Magenta
)

@Composable
fun MyPIDSTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun testLightTheme() {
    MyPIDSTheme {
        sample()
    }
}

@Preview(showBackground = true)
@Composable
fun testDarkTheme() {
    MyPIDSTheme(true) {
        sample()
    }
}

@Composable
fun sample() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    TextField("Hello Theme!", {}, Modifier.padding(10.dp), isError = false)
                    TextField("Bye-bye Error Theme!", {}, Modifier.padding(10.dp), isError = true)
                }
            }
            Button(onClick = {}) {
                Text("OK")
            }
        }
    }
}