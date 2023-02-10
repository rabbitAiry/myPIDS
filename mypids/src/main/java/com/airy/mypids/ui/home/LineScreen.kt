package com.airy.mypids.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.airy.mypids.Line
import com.airy.mypids.viewmodels.PidsViewModel
import com.airy.mypids.ui.components.ConfigRowOfContent
import com.airy.mypids.ui.components.FullWarningText
import com.airy.mypids.viewmodels.LineSearchViewModel
import kotlinx.coroutines.launch

private const val TAG = "LineScreen"

@Composable
fun LineScreen() {
    val searchVm: LineSearchViewModel = viewModel()
    val lineVm: PidsViewModel = viewModel()
    val navController = rememberNavController()

    val lineHasChosen = lineVm.lineInfo != null
    val strokeColor = if (lineHasChosen) Color.DarkGray else Color.LightGray
    Column(Modifier.fillMaxWidth()) {
        if (!lineHasChosen) { SearchBar(searchVm) }
        CorneredCard(strokeColor) {
            searchVm.run {
                if (lineHasChosen) LineResultText(lineVm.lineInfo!!.lineName!!) { lineVm.clearInfo() }  // 线路已存在
                else if (inPoiSearch) CircularProgressIndicator()    // 正在进行poi搜索
                else if (!inPoiSearch && poiSearchError!=null) FullWarningText(text = poiSearchError!!)   // poi搜索失败
                else if (resultPoiList!=null) SearchResultBox(searchVm)  // poi搜索成功
            }
        }
        Button(onClick = { navController.navigate(Line.route) { restoreState = true } }) {
            Text(text = "继续")
        }
    }
}

@Composable
fun SearchBar(vm: LineSearchViewModel) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Row() {
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
                    onValueChange = {vm.searchText = it},
                    placeholder = { Text(text = "1路") })
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (vm.searchTextNotNull()){
                            vm.getLinePoiSearch()
                        } else{
                            scope.launch { scaffoldState.snackbarHostState.showSnackbar("城市和线路名都不可以为空") } // TODO
                        }
                    },
                    contentPadding = PaddingValues(0.dp),
                    content = { Icon(Icons.Default.Search, "Search") }
                )
            }
        }
    }
}

@Composable
fun CorneredCard(strokeColor: Color, content: @Composable ()->Unit){
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .padding(10.dp),
        border = BorderStroke(2.dp, strokeColor),
        content = content
    )
}

@Composable
fun LineResultText(searchLineResult: String, onClearButtonClick: () -> Unit) {
    Row {
        Text(searchLineResult)
        IconButton(onClick = onClearButtonClick) {
            Icon(Icons.Default.Delete, "delete")
        }
    }
}

@Composable
fun SearchResultBox(vm: LineSearchViewModel) {
    LazyColumn {
        items(vm.resultPoiList!!) {
            Text(text = it.name, modifier = Modifier.clickable { vm.getLineSearch(it.uid) })
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLineScreen() {
    LineScreen()
}