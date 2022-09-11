package com.airy.mypids.utils

import com.airy.mypids.objects.Line
import com.airy.mypids.objects.Station
import com.baidu.mapapi.search.busline.BusLineResult

object LineUtil {
    fun baiduLineToAppLine(result: BusLineResult): Line {
        val stations = ArrayList<Station>()
        for (s in result.stations) {
            stations.add(Station(s.title, s.location.latitude, s.location.longitude))
        }
        return Line(
            splitBaiduLineName(result.busLineName),
            splitBaiduLineDirection(result.lineDirection),
            stations
        )
    }

    private fun splitBaiduLineName(origin: String): String = splitBefore(origin, '(')

    private fun splitBaiduLineDirection(origin: String): String = splitBefore(origin, '方')

    private fun splitBefore(origin: String, target: Char): String {
        var end = 0
        for ((index, c) in origin.withIndex()) {
            if (c == target) {
                end = index
                break
            }
        }
        return origin.substring(0, end)
    }
}