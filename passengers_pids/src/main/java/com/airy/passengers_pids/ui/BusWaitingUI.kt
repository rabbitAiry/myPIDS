package com.airy.passengers_pids.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airy.passengers_pids.PassengerViewModel
import com.airy.pids_lib.ui.components.ContentCard
import com.airy.pids_lib.ui.components.NextStepButton
import com.airy.pids_lib.ui.components.SummaryText

@Composable
fun BusWaitingScreen(vm: PassengerViewModel = viewModel(), onNext: ()->Unit){
    Column {
        ContentCard(maxHeight = 360) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = vm.line!!.stations[vm.startStationIdx].name)
                Row {
                    Text(text = vm.waitStations!![0].toString(), style = MaterialTheme.typography.h4)
                    Text(text = "站")
                }
            }
        }
        NextStepButton(text = "我已上车", enabled = true, onClick = onNext)
    }
}