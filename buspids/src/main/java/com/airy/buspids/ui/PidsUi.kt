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
import com.airy.pids_lib.ui.components.SummaryText
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationConfiguration
import com.baidu.mapapi.map.MyLocationData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections

const val SUMMARY_TEXT_SWITCH_DURATION = 4000L
const val LIGHT_FLASH_DURATION = 800L
const val SCROLL_DURATION = 4000L
const val LANGUAGE_CHANGE_DURATION = 6000L
const val SWITCH_ITEMS = 5

@Composable
fun PidsUI(
    line: LineState,
    postList: List<Post>,
    locationInfo: LocationInfo?,
    forecastTimeList: List<Int>?
) {
    Column {
        if (line.pidsStatus == PidsStatus.BUS_STATION_ARRIVED) {
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
                line.currIdx,
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
fun LineSummaryBar(
    lineName: String, terminal: Station, nextStation: Station?
) {
    var descriptionCn by remember { mutableStateOf("") }
    var descriptionEn by remember { mutableStateOf("") }
    LaunchedEffect(nextStation) {
        // 轮流展示下一站和终点站
        while (true) {
            descriptionCn = "${terminal.name}方向"
            descriptionEn = "Towards: ${terminal.englishName}"
            delay(SUMMARY_TEXT_SWITCH_DURATION)
            descriptionCn = nextStation?.run { "下一站：${nextStation.name}" } ?: "已到达终点站"
            descriptionEn = nextStation?.run {"Next: ${nextStation.englishName}"} ?: "Arrival of the Terminal"
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
    Text(text = "预计到达时间 E.T.A.",
        modifier
            .background(light_gray_200, RoundedCornerShape(50))
            .padding(6.dp))
}

@Composable
fun StationListUI(
    line: LineInfo,
    currIdx: Int,
    postList: List<Post>,
    modifier: Modifier,
    forecastTimeList: List<Int>?
) {
    val startStationIdx = line.startStationIdx
    val endStationIdx = line.endStationIdx
    val subStationList by remember { mutableStateOf(line.stations.subList(startStationIdx, endStationIdx+1)) }
    val subForecastTimeList = forecastTimeList?.subList(startStationIdx, endStationIdx+1)?.toList()

    var currPostIdx by remember { mutableStateOf(0) }
    LaunchedEffect(key1 = true) {
        while (currPostIdx < postList.size) {
            delay(postList[currPostIdx].postMillis.toLong())
            currPostIdx++
        }
    }
    if (currPostIdx < postList.size) {
        Row(modifier) {
            VerticalStationLazyColumn(Modifier.weight(3f), currIdx - startStationIdx, subStationList, subForecastTimeList, line.otherLineColors)
            PostBar(postList[currPostIdx], Modifier.weight(2f))
        }
    } else {
        Box(modifier) {
            HorizontalStationListColumn(currIdx - startStationIdx, subStationList, subForecastTimeList, line.otherLineColors)
            IllustrationBar(Modifier.align(Alignment.BottomStart))
        }
    }
}

@Composable
fun VerticalStationLazyColumn(
    modifier: Modifier,
    subCurrIdx: Int,
    subStationList: List<Station>,
    subForecastTimeList: List<Int>?,
    otherLineColors: Map<String, Color>
) {
    var listStartIdx by remember { mutableStateOf(subCurrIdx) }
    val lazyColumnState = rememberLazyListState()

    // 循环滚动站点列表
    LaunchedEffect(subCurrIdx) {
        listStartIdx = subCurrIdx
        while (true) {
            lazyColumnState.scrollToItem(listStartIdx)
            delay(SCROLL_DURATION)
            listStartIdx += SWITCH_ITEMS
            if (listStartIdx > subStationList.size) listStartIdx = subCurrIdx
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyColumnState,
        contentPadding = PaddingValues(10.dp),
        userScrollEnabled = false
    ) {
        itemsIndexed(subStationList) { index, station ->
            Row(modifier.height(80.dp)) {
                Row(
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (subForecastTimeList != null && index>=subCurrIdx && subForecastTimeList[index]!=0)Text(text = "${subForecastTimeList[index]/60}分钟")
                    Text(text = "${(index - subCurrIdx) + 1}站", Modifier.padding(horizontal = 4.dp))
                }
                Row(Modifier.weight(5f), verticalAlignment = Alignment.CenterVertically) {
                    val stationStatus = when {
                        index == subCurrIdx -> StationStatus.CURR
                        index > subCurrIdx -> StationStatus.UNREACHED
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
                                bgColor = otherLineColors[interchange] ?: gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostBar(post: Post, modifier: Modifier) {
    Column(modifier) {
        Card(
            modifier = Modifier
                .padding(end = 20.dp, top = 20.dp, bottom = 20.dp)
                .weight(1f)
                .padding(10.dp),
            border = BorderStroke(2.dp, dark_gray_100)
        ) {
            Text(text = post.content, style = MaterialTheme.typography.h5)
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
    subCurrIdx: Int,
    subStationList: List<Station>,
    subForecastTimeList: List<Int>?,
    otherLineColors: Map<String, Color>
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
                subStationList,
                subCurrIdx,
                modeCn,
                size,
                shine,
                subForecastTimeList,
                otherLineColors
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
