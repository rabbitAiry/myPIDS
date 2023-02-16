package com.airy.mypids.data

import androidx.compose.ui.graphics.Color

data class LineInfo(
    val lineName: String,
    val rawLineName: String,
    val lineDirection: String,
    var lineId: String,
    var lineColor: Color,
    var lineMapLeftToRight: Boolean = true,
    val otherLineColors: Map<String, Color>,
){}

/**
// name style
val briefLineName
get() = if (lineName.last() == '路') lineName.substring(0, lineName.length - 1) else lineName

val lineDescription get() = "$lineName ${terminusStation}方向"

// station
val startStation get() = stations[0]

val terminusStation get() = stations.last()

var currStation
get() = stations[currIdx]
set(value) {
currIdx = stations.indexOf(value)
}

val isLastStation get() = currIdx == stations.size - 1

val stationNames
get() = run {
val list = LinkedList<String>()
for (station in stations) list.add(station.name)
list
}

fun nextStation(): Int {
if (currIdx + 1 < stations.size) currIdx++
return currIdx
}
 */