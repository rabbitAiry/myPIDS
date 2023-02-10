package com.airy.mypids.ui.home


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import com.airy.mypids.pids.gz_metro_style.Station
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airy.mypids.ui.components.ConfigRowOfColorSelector
import com.airy.mypids.ui.components.ConfigRowOfTextField
import com.airy.mypids.utils.ColorUtil

val DEFAULT_COLOR = Color.White

@Composable
fun LineConfigScreen(modifier: Modifier = Modifier) {
    var lineId by remember { mutableStateOf("") }
    var colorString by remember { mutableStateOf("FFFFFF") }
    var color by remember { mutableStateOf(Color.White) }
    var isWrongColor by remember { mutableStateOf(true) }
    Column {
        LineDemo(lineId, color)
        ConfigLineId(lineId){ lineId = it }
        ConfigLineColor(color, colorString, isWrongColor){
            colorString = it
            val resColor = ColorUtil.parseColor(it)
            if (resColor != null) {
                isWrongColor = false
                color = resColor
            } else {
                isWrongColor = true
                color = DEFAULT_COLOR
            }
        }
    }
}

@Composable
fun LineDemo(lineId: String, color: Color){
    Card(Modifier.padding(20.dp).fillMaxWidth().border(2.dp, Color.DarkGray, RoundedCornerShape(10)).padding(20.dp)) {
        Row(horizontalArrangement = Arrangement.Center){
            // TODO: 换成对应线路的站名
            Station(name = "大学城南", lineId = lineId, stationId = "X", color = color, extraWidth = 50, isStart = true)
            Station(name = "板桥", lineId = lineId, stationId = "X", color = color, extraWidth = 50)
        }
    }
}

@Composable
private fun ConfigLineId(lineId: String, onValueChange: (String) -> Unit) {
    ConfigRowOfTextField(
        configTitle = "线路id",
        value = lineId,
        configDescription = "线路ID用于车站编号中表示线路的部分，比如三号线的线路ID为“3”",
        onValueChange = onValueChange
    )
}

@Composable
private fun ConfigLineColor(
    color: Color,
    colorString: String,
    isWrongColor: Boolean,
    onValueChange: (String)->Unit,
) {
    ConfigRowOfColorSelector(
        configTitle = "线路颜色",
        inputColor = color,
        inputColorString = colorString,
        configDescription = "输入十六进制RGB形式的颜色",
        onValueChange = onValueChange,
        isWrongColor = isWrongColor
    )
}

@Preview("LineNameConfig", backgroundColor = 0xffffff, showBackground = true)
@Composable
fun PreviewLineNameConfig() {
    LineConfigScreen()
}