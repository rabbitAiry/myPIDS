package com.airy.mypids.objects

data class StationListInfo(
    var stations: List<Station>,
    var currIdx: Int = 0,
    val languages: List<String>,
    val stationIdStart: Int = 0,
    val isStationIdIncrease: Boolean = true,
    val startStationIdx:Int = 0,
    val endStationIdx: Int = stations.size
)