package com.airy.buspids.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.PidsStatus
import com.airy.pids_lib.data.StationStatus
import com.airy.pids_lib.ui.gray

data class LineState(
    val lineInfo: LineInfo,
    var currIdx: MutableState<Int> = mutableStateOf(lineInfo.startStationIdx),
    var pidsStatus: MutableState<PidsStatus> = mutableStateOf(PidsStatus.BUS_STATION_ARRIVED),
    val stationStates: MutableList<StationStatus> = MutableList(lineInfo.stations.size) { idx ->
        when {
            idx > currIdx.value -> StationStatus.UNREACHED
            idx == currIdx.value -> StationStatus.CURR
            else -> StationStatus.ARRIVED
        }
    }
){
    val stations get() = lineInfo.stations
    val firstStation = stations[lineInfo.startStationIdx]
    val lastStation = stations[lineInfo.endStationIdx]
    val currStation get() = stations[currIdx.value]
    val nextStation get() = stations[currIdx.value+1]

    fun isLastStation(): Boolean = currIdx.value == stations.size-1

    operator fun get(num:Int) = stations[num]

    fun goNextStation(): Int{
        if (!isLastStation()){
            currIdx.value++
            stationStates[currIdx.value] = StationStatus.CURR
            stationStates[currIdx.value-1] = StationStatus.ARRIVED
        }
        return currIdx.value
    }

    fun stationArrived(){
        pidsStatus.value = PidsStatus.BUS_STATION_ARRIVED
    }

    fun busRun(){
        if (pidsStatus.value != PidsStatus.BUS_RUNNING){
            goNextStation()
            pidsStatus.value = PidsStatus.BUS_RUNNING
        }
    }
}