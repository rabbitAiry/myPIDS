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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airy.mypids.data.LineInfo
import com.airy.mypids.data.Station
import com.airy.pids_lib.ui.components.ConfigRowOfColorSelector
import com.airy.pids_lib.ui.components.ConfigRowOfTextField
import com.airy.pids_lib.ui.components.FinishStepsButton
import com.airy.mypids.data.PidsData

val DEFAULT_COLOR = Color.White

@Composable
fun LineConfigScreen(
    pids: PidsData,
    navController: NavHostController = rememberNavController()
) {
    val lineInfo = pids.lineInfo!!
    // TODO
    var lineId by remember { mutableStateOf(lineInfo.lineId) }
    var color by remember { mutableStateOf(lineInfo.lineColor) }
    var colorString by remember { mutableStateOf(lineInfo.lineColor.value.shr(8).toString(16)) }
    var isWrongColor by remember { mutableStateOf(true) }
    Column {
        LineDemo(lineInfo, pids.stationListInfo!!.stations)
        ConfigLineId(lineId) {
            pids.setLineId(it)
            lineId = it
        }
//        ConfigLineColor(color, colorString, isWrongColor) {
//            colorString = it
//            val resColor = ColorUtil.parseColor(it)
//            if (resColor != null) {
//                isWrongColor = false
//                color = resColor
//                vm.setLineColor(color)
//            } else {
//                isWrongColor = true
//                color = DEFAULT_COLOR
//            }
//        }
        FinishStepsButton(enabled = true, navController = navController)
    }
}

@Composable
fun LineDemo(line: LineInfo, stations: List<Station>) {
    Card(
        Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .border(2.dp, Color.DarkGray, RoundedCornerShape(10))
    ) {
        Row(Modifier.padding(20.dp), horizontalArrangement = Arrangement.Center) {
            Station(
                name = stations[0].name,
                lineId = line.lineId,
                stationId = "X",
                color = line.lineColor,
                extraWidth = 50,
                isStart = true
            )
            Station(
                name = stations[1].name,
                lineId = line.lineId,
                stationId = "X",
                color = line.lineColor,
                extraWidth = 50
            )
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
    onValueChange: (String) -> Unit,
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

}