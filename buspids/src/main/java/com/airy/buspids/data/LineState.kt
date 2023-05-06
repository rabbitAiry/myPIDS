package com.airy.buspids.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.PidsStatus

data class LineState(
    val lineInfo: LineInfo,
) {
    var currIdx: Int by mutableStateOf(lineInfo.startStationIdx)
    var pidsStatus: PidsStatus by mutableStateOf(PidsStatus.BUS_STATION_ARRIVED)

    val stations get() = lineInfo.stations
    val firstStation = stations[lineInfo.startStationIdx]
    val lastStation = stations[lineInfo.endStationIdx]
    val currStation get() = stations[currIdx]
    val nextStation get() = if (currIdx >= lineInfo.endStationIdx) null else stations[currIdx + 1]

    fun isLastStation(): Boolean = currIdx == lineInfo.endStationIdx

    operator fun get(num: Int) = stations[num]

    fun goNextStation(): LineState {
        if (!isLastStation()) {
            currIdx++
        }
        return this
    }

    fun stationArrived(): LineState {
        pidsStatus = PidsStatus.BUS_STATION_ARRIVED
        return this
    }

    fun busRun(): LineState {
        pidsStatus = PidsStatus.BUS_RUNNING
        return this
    }
}