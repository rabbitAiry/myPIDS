package com.airy.buspids.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airy.buspids.data.LocationInfo
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.ui.components.SummaryText
import com.airy.pids_lib.ui.golden_500
import com.airy.pids_lib.ui.light_gray_200
import kotlinx.coroutines.delay

@Composable
fun LineSummaryBar(
    lineName: String, terminal: Station, nextStation: Station
) {
    var descriptionCn by remember { mutableStateOf("") }
    var descriptionEn by remember { mutableStateOf("") }
    LaunchedEffect(nextStation) {
        // 轮流展示下一站和终点站
        while (true) {
            descriptionCn = "${terminal.name}方向"
            descriptionEn = "Towards: ${terminal.englishName}"
            delay(SUMMARY_TEXT_SWITCH_DURATION)
            descriptionCn = "下一站：${nextStation.name}"
            descriptionEn = "Next: ${nextStation.englishName}"
            delay(SUMMARY_TEXT_SWITCH_DURATION)
        }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .background(golden_500)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        SummaryText(text = lineName, isMain = true)
        Spacer(modifier = Modifier.width(24.dp))
        SummaryText(text = descriptionCn)
        Spacer(modifier = Modifier.widthIn(min= 24.dp, max=72.dp))
        SummaryText(text = descriptionEn)
    }
}

@Composable
fun IllustrationBar(modifier: Modifier = Modifier) {
    Text(text = "预计到达时间 E.T.A.", modifier.background(light_gray_200, RoundedCornerShape(50)).padding(4.dp))
}