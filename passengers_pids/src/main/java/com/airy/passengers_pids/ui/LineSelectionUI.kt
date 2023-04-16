package com.airy.passengers_pids.ui

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airy.passengers_pids.PassengerViewModel
import com.airy.pids_lib.ui.components.ConfigRowOfContent
import com.airy.pids_lib.ui.components.ContentCard
import com.airy.pids_lib.ui.components.FullWarningText
import com.airy.pids_lib.ui.components.NextStepButton

@Composable
fun LineSelectionScreen(viewModel: PassengerViewModel = viewModel(), onNext: ()->Unit) {
    val lineHasChosen = viewModel.line != null
    val strokeColor = if (lineHasChosen) Color.DarkGray else Color.LightGray
    Column(Modifier.fillMaxWidth()) {
        if (!lineHasChosen)
            SearchBar(viewModel)
        ContentCard(strokeColor) {
            Column(
                Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // 按结果倒叙
                if (lineHasChosen) LineResultText(viewModel.line!!.lineName) { viewModel.clearLine() }  // 搜完，线路存在
                else if (viewModel.resultPoiList != null) SearchResultBox(viewModel) // 搜完，poi结果存在。 或正在搜线路
                else if (viewModel.poiSearchError != null) FullWarningText(text = viewModel.poiSearchError!!)   // poi搜索失败
                else if (viewModel.inPoiSearch) CircularProgressIndicator() // 正在搜poi
            }
        }
        NextStepButton(enabled = lineHasChosen, onClick = onNext)
    }
}

@Composable
fun SearchBar(viewModel: PassengerViewModel) {
    val context = LocalContext.current
    Row {
        ConfigRowOfContent(configTitle = "城市、\n线路名称") { modifier ->
            Row(modifier) {
                TextField(
                    modifier = Modifier.weight(2f),
                    value = viewModel.searchCityText,
                    onValueChange = { viewModel.searchCityText = it },
                    placeholder = { Text(text = "城市") })
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    modifier = Modifier.weight(4f),
                    value = viewModel.searchLineText,
                    onValueChange = { viewModel.searchLineText = it },
                    placeholder = { Text(text = "线路") })
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.searchLinePoi {
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
fun SearchResultBox(vm: PassengerViewModel) {
    Column {
        LazyColumn(
            Modifier.fillMaxWidth()
        ) {
            items(vm.resultPoiList!!) {
                Text(text = it.name, modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        vm.searchLine(it.uid)
                    })
                Divider()
            }
        }
        if (vm.inLineSearch) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        else if (vm.lineSearchError != null) {
            FullWarningText(text = vm.lineSearchError!!)
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
