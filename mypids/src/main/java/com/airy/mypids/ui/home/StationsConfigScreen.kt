package com.airy.mypids.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airy.mypids.ui.components.ConfigRowOfSwitch
import com.airy.mypids.ui.components.ConfigRowOfTextField

@Composable
fun StationsConfigScreen() {
    var stationFirstId by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxWidth()) {
        ConfigRowOfTextField(
            configTitle = "站点初始id",
            value = stationFirstId.toString(),
            configDescription = "",
            onValueChange = { stationFirstId = it.toInt() })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStationsConfigScreen() {
    StationsConfigScreen()
}