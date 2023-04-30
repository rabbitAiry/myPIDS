package com.airy.buspids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.airy.buspids.repository.LogRepository
import com.airy.buspids.ui.PidsUI
import com.airy.buspids.ui.theme.Black50
import com.airy.buspids.ui.theme.Gray50
import com.airy.buspids.ui.theme.MyPIDSTheme
import com.airy.buspids.vm.PidsViewModel
import com.airy.pids_lib.utils.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PidsActivity : ComponentActivity() {
    lateinit var vm: PidsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()

        vm = ViewModelProvider(this)[PidsViewModel::class.java]
        setContent {
            MyPIDSTheme {
                var inDebug by remember { mutableStateOf(false) }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onDoubleTap = { inDebug = !inDebug })
                        },
                    color = MaterialTheme.colors.background
                ) {
                    Box(){
                        PidsUI(vm.line, vm.postList, vm.locationInfo, vm.forecastTimeList)
                        if (inDebug){
                            DebugScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DebugScreen() {
        Row(
            Modifier
                .fillMaxSize()
                .background(Black50)
                .padding(30.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Button(onClick = { vm.line.goNextStation() }) {
                    Text("下一站")
                }
                Button(onClick = { vm.stationArrived() }) {
                    Text("到站")
                }
                Button(onClick = { vm.stationLeave() }) {
                    Text("离站")
                }
            }
            Column(
                Modifier
                    .background(Gray50)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .weight(1f)
            ) {
                LazyColumn{
                    items(LogRepository.logLineList){
                        Text(text = it, Modifier.padding(2.dp))
                    }
                }
            }
            Column(
                Modifier
                    .background(Gray50)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .weight(1f)
            ) {
                LazyColumn{
                    items(LogRepository.logPositionList){
                        Text(text = it, Modifier.padding(2.dp))
                    }
                }
            }
        }
    }
}