package com.airy.buspids.ui

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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airy.buspids.data.LineState
import com.airy.buspids.data.LocationInfo
import com.airy.pids_lib.data.*
import com.airy.pids_lib.ui.*
import com.airy.pids_lib.ui.components.DescriptionText
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SUMMARY_TEXT_SWITCH_DURATION = 4000L
const val LIGHT_FLASH_DURATION = 1000L
const val SCROLL_DURATION = 4000L
const val LANGUAGE_CHANGE_DURATION = 6000L
const val SWITCH_ITEM_COUNT = 5
const val SWITCH_MAP_DURATION = 20000L

@Composable
fun PidsUI(
    line: LineState,
    postList: List<Post>,
    locationInfo: LocationInfo?,
    forecastTimeList: List<Int>
) {
    Column {
        if (line.pidsStatus.value == PidsStatus.BUS_STATION_ARRIVED) {
            StationArrivedBar(
                currStation = line.currStation,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
                locationInfo
            )
            LineSummaryBar(
                lineName = line.lineInfo.rawLineName,
                terminal = line.lastStation,
                nextStation = line.nextStation
            )
        } else {
            StationListUI(
                line.lineInfo,
                line.currIdx.value,
                line.stationStates,
                postList,
                Modifier.weight(1F),
                forecastTimeList
            )
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
    modifier: Modifier,
    forecastTimeList: List<Int>
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
            VerticalStationLazyColumn(line, currIdx, Modifier.weight(3f), forecastTimeList)
            PostBar(postList[currPostIdx], line, currIdx, Modifier.weight(2f))
        }
    } else {
        Box(modifier) {
            HorizontalStationListColumn(line, stationStates, forecastTimeList)
            IllustrationBar(Modifier.align(Alignment.BottomStart))
        }
    }
}

@Composable
fun VerticalStationLazyColumn(
    line: LineInfo,
    currIdx: Int,
    modifier: Modifier,
    forecastTimeList: List<Int>
) {
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
        itemsIndexed(line.stations) { index, station ->
            Row(modifier.height(80.dp)) {
                Row(
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.End
                ) {
                    val diff = (index - currIdx) + 1
                    if (forecastTimeList.isNotEmpty())Text(text = "${forecastTimeList[currIdx]}分钟")
                    Text(text = "${diff}站", Modifier.padding(horizontal = 4.dp))
                }
                Row(Modifier.weight(5f), verticalAlignment = Alignment.CenterVertically) {
                    val stationStatus = when {
                        index == currIdx -> StationStatus.CURR
                        index > currIdx -> StationStatus.UNREACHED
                        else -> StationStatus.ARRIVED
                    }
                    VerticalStationIndicator(stationStatus)
                    Column {
                        Text(text = station.name, style = MaterialTheme.typography.h6)
                        Text(text = station.englishName, style = MaterialTheme.typography.h6)
                    }
                    station.interchanges?.let {
                        for (interchange in it) {
                            MetroIndicator(
                                text = interchange,
                                bgColor = line.getLineColor(interchange)
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
    Column(modifier) {
        Card(
            modifier = Modifier
                .padding(end = 20.dp, top = 20.dp, bottom = 20.dp)
                .weight(1f)
                .padding(10.dp),
            border = BorderStroke(2.dp, dark_gray_100)
        ) {
            when (post) {
                is NormalPost -> {
                    Text(text = post.content, style = MaterialTheme.typography.h5)
                }
                is StationPost -> {
                    val stationText = if (line.startStationIdx < currIdx
                        && currIdx < line.endStationIdx
                        && currIdx < post.stationIdxBefore
                    ) {
                        line.stations[post.stationIdxBefore].name
                    } else if (currIdx == post.stationIdxBefore) {
                        "本站"
                    } else null

                    stationText?.let {
                        Column {
                            Text(
                                text = "前往${post.content}的乘客可在",
                                style = MaterialTheme.typography.h5
                            )
                            Row {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.h4,
                                    color = golden_800
                                )
                                Text(text = "下车", style = MaterialTheme.typography.h5)
                            }
                        }
                    }
                }
            }
        }
        IllustrationBar(
            Modifier
                .padding(bottom = 10.dp, end = 10.dp)
                .align(Alignment.End)
        )
    }
}

@Composable
private fun HorizontalStationListColumn(
    line: LineInfo,
    stationStates: List<StationStatus>,
    forecastTimeList: List<Int>
) {
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
                shine,
                forecastTimeList
            )
        }
    }
}

@Composable
fun VerticalStationIndicator(stationStatus: StationStatus) {
    var color by remember {
        mutableStateOf(
            when (stationStatus) {
                StationStatus.ARRIVED -> Color.Gray
                StationStatus.CURR -> golden_400
                StationStatus.UNREACHED -> golden_400
                StationStatus.NONE -> Color.Transparent
            }
        )
    }
    val lineColor by remember {
        mutableStateOf(
            when (stationStatus) {
                StationStatus.ARRIVED -> Color.Gray
                else -> golden_400
            }
        )
    }
    LaunchedEffect(key1 = stationStatus) {
        while (stationStatus == StationStatus.CURR) {
            color = if (color == golden_600) red_600 else golden_600
            delay(LIGHT_FLASH_DURATION)
        }
    }
    Canvas(
        modifier = Modifier
            .width(30.dp)
            .height(80.dp)
    ) {
        drawLine(
            lineColor,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            strokeWidth = 12.dp.toPx()
        )
        drawCircle(color, 14.dp.toPx(), Offset(size.width / 2, size.height / 2))
    }
}

@Composable
fun MetroIndicator(text: String, bgColor: Color) {
    Text(
        text = text,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(4.dp),
        color = if (bgColor.luminance() > 0.5) Color.Black else Color.White
    )
}

private val locationConfiguration =
    MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null).apply {
        setAnimation(true)
    }

@Composable
fun StationArrivedBar(currStation: Station, modifier: Modifier, locationInfo: LocationInfo?) {
    Box(modifier) {
        locationInfo?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5F)
                    .align(Alignment.TopEnd)
                    .drawWithCache {
                        val brush = Brush.horizontalGradient(
                            colorStops = arrayOf(0.0f to Color.White, 0.3f to Color.Transparent)
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush)
                        }
                    },
            ){
                Text(text = "hi")
                MapWithLocationBar(
                    Modifier.fillMaxSize(),
                    it.latitude,
                    it.longitude,
                    it.direction,
                    it.radius
                )   
            }
        }
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
fun MapWithLocationBar(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    direction: Float,
    accuracy: Float
) {
    AndroidView(modifier = modifier, factory = { context ->
        MapView(context).apply {
            map.isMyLocationEnabled = true
            map.setMyLocationConfiguration(locationConfiguration)
            map.setMapStatus(MapStatusUpdateFactory.zoomBy(6F))
        }
    }, update = {
        it.map.setMyLocationData(
            MyLocationData.Builder().accuracy(accuracy).latitude(latitude).longitude(longitude)
                .direction(direction).build()
        )
    })
}

val Station.englishName get() = names[1]
