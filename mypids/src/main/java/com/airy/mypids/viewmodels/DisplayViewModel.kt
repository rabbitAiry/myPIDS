package com.airy.mypids.viewmodels

import android.util.Log
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
    val stationStatus = MutableList(PidsData.stationListInfo!!.stations.size) {
        val currIdx = PidsData.stationListInfo!!.currIdx
        mutableStateOf(
            when {
                it > currIdx -> StationStatus.UNREACHED
                it == currIdx -> StationStatus.CURR
                else -> StationStatus.ARRIVED
            }
        )
    }

    var pidsStatus by mutableStateOf(PidsStatus.BUS_STATION_ARRIVED)

    fun nextStation() {
        val currIdx = PidsData.stationListInfo!!.nextStation()
        stationStatus[currIdx].value = StationStatus.CURR
        stationStatus[currIdx - 1].value = StationStatus.ARRIVED
        Log.d(TAG, "nextStation: $currIdx")
    }

    fun stationArrived() {
        pidsStatus = PidsStatus.BUS_STATION_ARRIVED
    }

    fun busRun() {
        pidsStatus = PidsStatus.BUS_RUNNING
    }
}