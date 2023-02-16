package com.airy.mypids.data

import android.util.Log

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

    operator fun get(num:Int) = stations[num]

    fun isLastStation(): Boolean = currIdx == stations.size-1

    fun nextStation(){
        if (isLastStation()) return
        currIdx++
        Log.d(TAG, "nextStation: $currIdx")
        Log.d(TAG, "nextStation: ${currStation.name}")
    }
}