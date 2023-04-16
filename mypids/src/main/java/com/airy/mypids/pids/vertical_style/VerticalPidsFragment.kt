package com.airy.mypids.pids.vertical_style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airy.mypids.data.PidsData
import com.airy.mypids.pids.StationStatus
import com.airy.mypids.ui.theme.light_blue_400
import com.airy.mypids.ui.theme.light_blue_600
import com.airy.mypids.viewmodels.DisplayViewModel

@Composable
fun VerticalPidsScreen(vm: DisplayViewModel) {
    val lineInfo = PidsData.lineInfo!!
    val stations = PidsData.stationListInfo!!
    Column(Modifier.fillMaxSize()) {
        LineInfoBar(lineInfo.rawLineName, "${lineInfo.lineDirection}方向")
        NotificationBar("")
        StationListBar(stations, vm.stationStates)
    }
}

@Composable
fun LineInfoBar(lineName: String, lineDirection: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(light_blue_600)
            .padding(horizontal = 14.dp, vertical = 48.dp)
    ) {
        Text(text = lineName, style = MaterialTheme.typography.h3)
        Text(text = lineDirection, style = MaterialTheme.typography.h6)
    }
}

@Composable
fun NotificationBar(content: String, isNotice: Boolean = false) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(light_blue_400)
    ) {
        Text(
            text = content,
            Modifier.padding(10.dp),
            style = MaterialTheme.typography.h6,
            maxLines = 1
        )
    }
}

@Composable
fun StationListBar(stations: StationListInfo, stationStatus: List<StationStatus>) {
    val state = rememberLazyListState()
    LaunchedEffect(key1 = stationStatus) {
        state.scrollToItem(stations.currIdx + 6)
    }
    LazyColumn(userScrollEnabled = false) {
        itemsIndexed(stationStatus) { index, status ->
            Station(
                name = stations[index].name,
                status = status
            )
        }
    }
}