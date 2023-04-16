package com.airy.mypids.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.airy.mypids.data.PidsData
import com.airy.mypids.pids.PidsStatus
import com.airy.mypids.pids.StationStatus

private const val TAG = "DisplayViewModel"

class DisplayViewModel : ViewModel() {
    val stationStates =
        MutableList(PidsData.stationListInfo!!.stations.size) { StationStatus.UNREACHED }.toMutableStateList()

    val currStationIdx get() = PidsData.stationListInfo!!.currIdx

    val notice = "尊老爱幼是中华民族的传统美德，请您为有需要的乘客让座，谢谢！"

    var pidsStatus by mutableStateOf(PidsStatus.BUS_STATION_ARRIVED)

    fun nextStation() {
        PidsData.stationListInfo!!.nextStation()
        stationStates[currStationIdx] = StationStatus.CURR_ARRIVED
        stationStates[currStationIdx - 1] = StationStatus.ARRIVED
    }

    fun stationArrived() {
        pidsStatus = PidsStatus.BUS_STATION_ARRIVED
        stationStates[currStationIdx] = StationStatus.CURR_ARRIVED
    }

    fun busRun() {
        if (pidsStatus != PidsStatus.BUS_RUNNING) {
            nextStation()
            pidsStatus = PidsStatus.BUS_RUNNING
            stationStates[currStationIdx] = StationStatus.CURR_UNREACHED
        }
    }
}