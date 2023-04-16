package com.airy.passengers_pids.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airy.passengers_pids.PassengerViewModel
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.ui.components.*

/**
 * 临时措施
 * 上下车只能填数字
 */
@Composable
fun TerminalSelectionScreen(vm: PassengerViewModel = viewModel(), onNext: () -> Unit) {
    Column {
        ConfigRowOfTextField(configTitle = "选择起点", value = vm.startStationIdx.toString(), configDescription = null, onValueChange = {vm.startStationIdx = it.toInt()})
        vm.waitStations?.let {
            LineWaitTimeBar(lineName = vm.line!!.lineName, waitStations = it)
        }
        ConfigRowOfTextField(configTitle = "选择终点", value = vm.destinationIdx.toString(), configDescription = null, onValueChange = {vm.destinationIdx = it.toInt()})
        ConfigRowOfSwitch(
            configTitle = "下车提醒",
            isChecked = vm.getOffRemind,
            configDescription = null,
            onValueChange = { vm.getOffRemind = it })
        NextStepButton(enabled = vm.startStationIdx!=vm.destinationIdx, onClick = onNext)
    }
}

@Composable
fun LineWaitTimeBar(lineName: String, waitStations: List<Int>) {
    ContentCard {
        Column {
            SummaryText(text = lineName, true)
            LazyRow{
                itemsIndexed(waitStations){index, item ->  
                    Column(Modifier.border(1.dp, Color.LightGray)) {
                        SummaryText(text = "$item 分钟")
                        DescriptionText(description = "第$index 班车")
                    }
                }
            }
        }
    }
}