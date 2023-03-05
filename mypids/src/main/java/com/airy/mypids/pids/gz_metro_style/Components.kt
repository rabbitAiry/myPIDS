package com.airy.mypids.pids.gz_metro_style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Station(
    name: String,
    lineId: String,
    stationId: String,
    color: Color,
    extraWidth: Int = 20,
    isStart: Boolean = false,
    isEnd: Boolean = false
) {
    val wholeWidth = extraWidth + 50
    Column() {
        Box(modifier = Modifier
            .width(wholeWidth.dp)
            .padding(vertical = 2.dp)
            .drawWithContent {
                if (!isStart) drawLine(
                    color,
                    Offset(0f, size.height / 2),
                    Offset(size.width / 2, size.height / 2),
                    4.dp.toPx()
                )
                if (!isEnd) drawLine(
                    color,
                    Offset(size.width / 2, size.height / 2),
                    Offset(size.width, size.height / 2),
                    4.dp.toPx()
                )
                drawContent()
            }) {
            Surface(
                Modifier
                    .width(50.dp)
                    .height(20.dp)
                    .drawWithContent {
                        drawContent()
                        drawRoundRect(
                            color,
                            cornerRadius = CornerRadius(50f, 50f),
                            style = Stroke(2.dp.toPx())
                        )
                        drawLine(
                            color,
                            Offset(size.width / 2, 0f),
                            Offset(size.width / 2, size.height),
                            2.dp.toPx()
                        )
                    }
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(100))
            ) {
                Row(Modifier.background(Color.White, RoundedCornerShape(100))) {
                    StationNumText(text = lineId)
                    StationNumText(text = stationId)
                }
            }
        }
        Text(name, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun RowScope.StationNumText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .weight(1f, true),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF222222)
@Composable
fun PreviewStation() {
    Station("大学城南", "7", "01", Color.Green, isStart = true)
}