package com.airy.mypids

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airy.mypids.objects.LineInfo
import com.baidu.mapapi.search.core.PoiInfo

@Composable
fun LineConfiguration(
    resultList: List<PoiInfo>? = null,
    lineInfo: LineInfo?,
    status: LineConfigurationStatus,
    onSearch: (String, String) -> Unit,
    onLineSelected: (PoiInfo) -> Unit,
    onClearStatus: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        if (status != LineConfigurationStatus.CHOSEN) {
            LineSearchBar(onSearch)
        }
        if (status == LineConfigurationStatus.IN_CHOOSE) {
            LineResultList(resultList, onLineSelected)
        }
        if (status == LineConfigurationStatus.CHOSEN) {
            SelectedLine(lineInfo, onClearStatus)
        }
    }
}

@Composable
fun LineSearchBar(onSearch: (String, String) -> Unit) {
    // TODO: 将测试参数删除
    var cityText by rememberSaveable { mutableStateOf("广州") }
    var lineText by rememberSaveable { mutableStateOf("B4") }

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(2f),
                value = cityText,
                onValueChange = { cityText = it },
                label = { Text("城市") },
                colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.LightGray),
//                isError = cityText.isEmpty()
            )
            OutlinedTextField(
                modifier = Modifier
                    .weight(5f)
                    .padding(horizontal = 6.dp),
                value = lineText,
                onValueChange = { lineText = it },
                label = { Text("线路名称") },
//                isError = lineText.isEmpty()
            )
            BlackFilledTextButton(
                onClick = {
                    cityText.trim()
                    lineText.trim()
                    onSearch(cityText, lineText)
                },
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .align(Alignment.Bottom)
                    .padding(start = 20.dp)
            ) { Text(text = "搜索") }
        }
        BlackFilledTextButton(onClick = {}) { Text("已保存的自定义线路") }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LineResultList(resultList: List<PoiInfo>?, onLineSelected: (PoiInfo) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(160.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        if (resultList == null) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        } else {
            if (resultList.isEmpty()) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    WarningMessage()
                }
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    items(resultList) { poi ->
                        Surface(onClick = {
                            onLineSelected(poi)
                        }) {
                            Text(text = poi.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectedLine(lineInfo: LineInfo?, onClearStatus: () -> Unit) {
    var dialogShow by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            if (lineInfo == null) {
                WarningMessage()
            } else {
                Text(
                    text = lineInfo.lineDescription,
                    style = MaterialTheme.typography.h5
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = onClearStatus
            ) {
                Icon(Icons.Rounded.Clear, "取消选择")
            }
        }
        if (lineInfo != null) {
            Row(Modifier.align(Alignment.End)) {
                Text("选择所在站点：", color = Color.DarkGray, modifier = Modifier.alignByBaseline())
                BlackFilledTextButton(modifier = Modifier.alignByBaseline(), onClick = {
                    dialogShow = true
                }) {
                    Text(lineInfo.currStation.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            }
            BlackFilledTextButton(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.End),
                onClick = {
                    // todo
                }) { Text("自定义线路") }
        }
    }
    if (dialogShow) {
        AlertDialog(onDismissRequest = { dialogShow = false }, buttons = {
            LazyColumn(
                Modifier
                    .height(600.dp)
                    .width(320.dp)
                    .padding(10.dp)
            ) {
                lineInfo!!.let {
                    items(it.stations) { station ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                it.currStation = station
                                dialogShow = false
                            }) {
                            Text(station.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        }
                    }
                }
            }
        })
    }
}