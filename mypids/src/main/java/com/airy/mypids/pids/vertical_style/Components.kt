package com.airy.mypids.pids.vertical_style

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@Composable
fun Station(
    name: String,
    status: StationStatus
) {
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(if (status == StationStatus.CURR) light_blue_100 else Color.White)
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
    Station(name = "远安路", status = StationStatus.CURR)
}