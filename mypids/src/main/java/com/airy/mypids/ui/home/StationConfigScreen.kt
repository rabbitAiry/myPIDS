package com.airy.mypids.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airy.mypids.ui.components.ColorSelector

@Composable
fun LineNameConfig(){
    var lineId by rememberSaveable { mutableStateOf("") }
    var colorText by rememberSaveable { mutableStateOf("888888") }
    Column{
        OutlinedTextField(
            modifier = Modifier,
            value = lineId,
            onValueChange = { lineId = it },
            label = { Text("线路id") },
        )
        Text(text = "线路ID用于车站编号中表示线路的部分，比如三号线的线路ID为“3”")
        ColorSelector(labelText = "设置线路颜色", colorText = colorText, onColorTextChange = {
            colorText = it
        })
    }
}

@Preview("LineNameConfig", backgroundColor = 0xffffff, showBackground = true)
@Composable
fun PreviewLineNameConfig(){
    LineNameConfig()
}