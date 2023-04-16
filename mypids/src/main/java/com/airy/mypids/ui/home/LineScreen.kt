package com.airy.mypids.ui.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airy.mypids.data.PidsData
import com.airy.pids_lib.ui.components.ConfigRowOfContent
import com.airy.pids_lib.ui.components.FullWarningText
import com.airy.mypids.viewmodels.LineSearchViewModel
import com.airy.pids_lib.ui.components.NextStepButton

private const val TAG = "LineScreen"

@Composable
fun LineScreen(pids: PidsData, navController: NavHostController) {
    val searchVm: LineSearchViewModel = viewModel()

    val lineHasChosen = pids.lineInfo != null
    val strokeColor = if (lineHasChosen) Color.DarkGray else Color.LightGray
    Column(Modifier.fillMaxWidth()) {
        if (!lineHasChosen) {
            SearchBar(searchVm)
        }
        CorneredCard(strokeColor) {
            searchVm.run {
                if (lineHasChosen) LineResultText(pids.lineInfo!!.lineName) { pids.clearLine() }  // 线路已存在
                else if (inPoiSearch) CircularProgressIndicator()    // 正在进行poi搜索
                else if (!inPoiSearch && poiSearchError != null) FullWarningText(text = poiSearchError!!)   // poi搜索失败
                else if (resultPoiList != null) SearchResultBox(searchVm, pids)  // poi搜索成功
            }
        }
        NextStepButton(
            enabled = lineHasChosen,
            onClick = {

            }
        )
    }
}

@Composable
fun SearchBar(vm: LineSearchViewModel) {
    Row{
        val context = LocalContext.current
        ConfigRowOfContent(configTitle = "城市、\n线路名称") { modifier ->
            Row(modifier) {
                TextField(
                    modifier = Modifier.weight(2f),
                    value = vm.searchCity,
                    onValueChange = { vm.searchCity = it },
                    placeholder = { Text(text = "广州") })
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    modifier = Modifier.weight(4f),
                    value = vm.searchText,
                    onValueChange = { vm.searchText = it },
                    placeholder = { Text(text = "1路") })
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (vm.searchTextNotNull()) {
                            vm.getLinePoiSearch()
                        } else {
                            Toast.makeText(context, "城市和线路名都不可以为空", Toast.LENGTH_SHORT).show()
                        }
                    },
                    contentPadding = PaddingValues(10.dp),
                    content = { Icon(Icons.Default.Search, "Search") }
                )
            }
        }
    }
}

@Composable
fun CorneredCard(strokeColor: Color, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .heightIn(min = 100.dp, max = 240.dp)
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, strokeColor),
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 2.dp), verticalArrangement = Arrangement.Center) {
            content()
        }
    }
}

@Composable
fun LineResultText(searchLineResult: String, onClearButtonClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceAround) {
        Text(searchLineResult, Modifier.align(Alignment.CenterVertically))
        IconButton(onClick = onClearButtonClick) {
            Icon(Icons.Default.Delete, "delete")
        }
    }
}

@Composable
fun SearchResultBox(searchVm: LineSearchViewModel, pidsVm: PidsData) {
    Column {
        LazyColumn(
            Modifier.fillMaxWidth()
        ) {
            items(searchVm.resultPoiList!!) {
                Text(text = it.name, modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        pidsVm.clearLine()
                        searchVm.getLineSearch(it.uid) { lineInfo, stationListInfo ->
                            pidsVm.setLine(lineInfo, stationListInfo)
                        }
                    })
                Divider()
            }
        }
        searchVm.run {
            if (inLineSearch) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            else if (!inLineSearch && lineSearchError != null) {
                FullWarningText(text = lineSearchError!!)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLineScreen() {
    LineScreen(PidsData, navController = rememberNavController())
}
