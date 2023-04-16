package com.airy.buspids.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.airy.buspids.constants.UI_TAG
import com.airy.buspids.data.LineState
import com.airy.pids_lib.data.*
import com.airy.pids_lib.ui.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SUMMARY_TEXT_SWITCH_DURATION = 4000L
const val LIGHT_FLASH_DURATION = 1000L
const val SCROLL_DURATION = 4000L
const val LANGUAGE_CHANGE_DURATION = 6000L
const val SWITCH_ITEM_COUNT = 5

@Composable
fun PidsUI(line: LineState, postList: List<Post>) {
    Column {
        if (line.pidsStatus.value == PidsStatus.BUS_STATION_ARRIVED) {
            StationArrivedBar(currStation = line.currStation, modifier = Modifier.weight(1F))
            LineSummaryBar(
                lineName = line.lineInfo.rawLineName,
                terminal = line.lastStation,
                nextStation = line.nextStation
            )
        } else {
            StationListUI(line.lineInfo, line.currIdx.value, line.stationStates, postList, Modifier.weight(1F))
            LineSummaryBar(
                lineName = line.lineInfo.rawLineName,
                terminal = line.lastStation,
                nextStation = line.currStation
            )
        }
    }
}

@Composable
fun StationListUI(
    line: LineInfo,
    currIdx: Int,
    stationStates: List<StationStatus>,
    postList: List<Post>,
    modifier: Modifier
) {
    var currPostIdx by remember { mutableStateOf(0) }
    LaunchedEffect(key1 = true) {
        while (currPostIdx < postList.size) {
            delay(postList[currPostIdx].duration.toLong())
            currPostIdx++
        }
    }
    if (currPostIdx < postList.size) {
        Row(modifier) {
            VerticalStationLazyColumn(line, currIdx, Modifier.weight(3f))
            PostBar(postList[currPostIdx], line, currIdx, Modifier.weight(2f))
        }
    } else {
        Box(modifier) {
            HorizontalStationListColumn(line, stationStates)
            IllustrationBar(Modifier.align(Alignment.BottomStart))
        }
    }
}

@Composable
fun VerticalStationLazyColumn(line: LineInfo, currIdx: Int, modifier: Modifier) {
    val lazyColumnState = rememberLazyListState()
    var startIdx by remember { mutableStateOf(currIdx) }

    // 循环滚动站点列表
    LaunchedEffect(currIdx) {
        while (true) {
            lazyColumnState.scrollToItem(startIdx)
            delay(SCROLL_DURATION)
            startIdx += SWITCH_ITEM_COUNT
            if (startIdx > line.endStationIdx) startIdx = currIdx
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyColumnState,
        contentPadding = PaddingValues(10.dp),
        userScrollEnabled = false
    ) {
        item{
            Row{
                IllustrationBar(Modifier.weight(1f))
                Row(Modifier.weight(4f)) {
                    VerticalStationIndicator(StationStatus.NONE)
                }
            }
        }
        itemsIndexed(line.stations) { index, station ->
            Row {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
                    val diff = (index - currIdx) + 1
                    Text(text = "${1 + diff * 2}分钟")
                    Text(text = "${diff}站", Modifier.padding(horizontal = 4.dp))
                }
                Row(Modifier.weight(4f)) {
                    VerticalStationIndicator(if (index == currIdx) StationStatus.CURR else StationStatus.UNREACHED)
                    Text(text = station.name)
                    station.interchanges?.let {
                        for (interchange in it) {
                            MetroIndicator(
                                text = interchange,
                                color = line.getLineColor(interchange)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostBar(post: Post, line: LineInfo, currIdx: Int, modifier: Modifier) {
    Card(modifier = modifier.padding(10.dp), border = BorderStroke(2.dp, dark_gray_100)) {
        when (post) {
            is NormalPost -> {
                Text(text = post.content)
            }
            is StationPost -> {
                val stationText = if (currIdx < post.stationIdxBefore) {
                    line.stations[currIdx].name
                } else if (currIdx == post.stationIdxBefore) {
                    "本站"
                } else return@Card

                Column {
                    Text(text = "前往${post.content}的乘客可在")
                    Row {
                        Text(text = stationText, style = MaterialTheme.typography.h5)
                        Text(text = "下车")
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalStationListColumn(line: LineInfo, stationStates: List<StationStatus>) {
    var modeCn by remember { mutableStateOf(true) }
    var shine by remember { mutableStateOf(true) }
    LaunchedEffect(true) {
        launch {
            while (true) {
                delay(LANGUAGE_CHANGE_DURATION)
                modeCn = !modeCn
            }
        }
        launch {
            while (true) {
                delay(LIGHT_FLASH_DURATION)
                shine = !shine
            }
        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->
            drawHorizontalLineMap(
                canvas.nativeCanvas,
                line,
                stationStates,
                modeCn,
                size,
                density,
                shine
            )
        }
    }
}

@Composable
fun StationArrivedBar(currStation: Station, modifier: Modifier) {
    Box(modifier) {
        // map
        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(32.dp)
        ) {
            DescriptionText(text = "当前到站 Arrival", Modifier.weight(1f))
            Column(Modifier.weight(2f)) {
                Text(text = currStation.name, style = MaterialTheme.typography.h3)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = currStation.englishName, style = MaterialTheme.typography.h4)
            }
        }
    }
}


@Composable
fun VerticalStationIndicator(stationStatus: StationStatus) {
    var color by remember { mutableStateOf(golden_400) }
    LaunchedEffect(key1 = stationStatus) {
        while (stationStatus == StationStatus.CURR) {
            color = if (color == golden_600) red_500 else golden_600
            delay(LIGHT_FLASH_DURATION)
        }
    }
    Canvas(
        modifier = Modifier
            .height(50.dp)
            .width(80.dp)
    ) {
        drawLine(
            gray,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = 8.dp.toPx()
        )
        drawCircle(color, 14.dp.toPx(), Offset(size.width / 2, size.height / 2))
    }
}

@Composable
fun MetroIndicator(text: String, color: Color) {
    Text(text = text, Modifier.background(color, RoundedCornerShape(50)))
}

val Station.englishName get() = names[1]
