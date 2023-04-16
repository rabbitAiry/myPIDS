package com.airy.mypids.pids.vertical_style

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airy.mypids.pids.StationStatus
import com.airy.mypids.ui.theme.light_blue_100
import com.airy.mypids.ui.theme.light_blue_400
import com.airy.mypids.ui.theme.light_blue_500
import com.airy.mypids.ui.theme.light_blue_600
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun Station(
    name: String,
    status: StationStatus
) {
    var stationColor by remember{ mutableStateOf(Color.Transparent) }
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    LaunchedEffect(key1 = status){
        when(status){
            StationStatus.ARRIVED -> {
                stationColor = light_blue_400
                backgroundColor = Color.Transparent
            }
            StationStatus.CURR_ARRIVED -> {
                stationColor = light_blue_600
                backgroundColor = light_blue_100
            }
            StationStatus.CURR_UNREACHED -> {
                backgroundColor = light_blue_100
                delay(10000)
            }
            StationStatus.UNREACHED -> {
                stationColor = Color.Transparent
                backgroundColor = Color.Transparent
            }
        }
    }
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(if (status == StationStatus.CURR_UNREACHED || status == StationStatus.CURR_ARRIVED) light_blue_100 else Color.White)
    ) {
        StationIndicator(status)
        Text(
            text = name,
            Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            fontSize = 22.sp
        )
    }
}

@Composable
fun StationIndicator(status: StationStatus) {
    val mainColor = light_blue_500
    val centralColor = if (status == StationStatus.ARRIVED) light_blue_400 else Color.White
//    LaunchedEffect(key1 = status){
//        while(status == StationStatus.CURR){
//            centralColor = if (centralColor==Color.White) light_blue_400 else Color.White
//            delay(1000)
//        }
//    }
    Canvas(modifier = Modifier
        .height(50.dp)
        .width(80.dp)) {
        drawLine(
            mainColor,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = 8.dp.toPx()
        )
        drawCircle(mainColor, 14.dp.toPx(), Offset(size.width / 2, size.height/2))
        drawCircle(centralColor, 8.dp.toPx(), Offset(size.width / 2, size.height/2))
    }
}

@Preview
@Composable
fun PreviewStation(){
    Station(name = "远安路", status = StationStatus.CURR_UNREACHED)
}