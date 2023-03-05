package com.airy.mypids.data

import com.airy.mypids.pids.StationStatus

private const val TAG = "StationListInfo"
data class StationListInfo(
    var stations: List<Station>,
    var currIdx: Int = 0,
    val languages: List<String>,
    val stationIdStart: Int = 0,
    val isStationIdIncrease: Boolean = true,
    val startStationIdx:Int = 0,
    val endStationIdx: Int = stations.size
){
    val currStation get() = stations[currIdx]
    val firstStation = stations.first()
    val terminal = stations.last()

    fun isLastStation(): Boolean = currIdx == stations.size-1

    operator fun get(num:Int) = stations[num]

    fun nextStation(): Int{
        if (!isLastStation()) currIdx++
        return currIdx
    }
}