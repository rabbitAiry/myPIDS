package com.airy.mypids.utils

import android.util.Log
import com.airy.mypids.objects.Line
import com.airy.mypids.objects.Station
import com.baidu.mapapi.search.busline.BusLineResult
import java.util.Collections

object LineUtil {
    val fakeLine1: Line = Line(
        "811路", "芳村西塱总站", listOf(
            Station("兴民路总站", 0.0, 0.0),
            Station("兴国路", 0.0, 0.0),
            Station("猎德", 0.0, 0.0),
            Station("花城大道", 0.0, 0.0),
            Station("花城大道（华穗路口）", 0.0, 0.0),
            Station("五羊邨", 0.0, 0.0),
            Station("长城大厦", 0.0, 0.0),
            Station("培正路", 0.0, 0.0),
            Station("新河浦路", 0.0, 0.0),
            Station("东湖路", 0.0, 0.0),
            Station("仲恺路", 0.0, 0.0),
            Station("怡安花园晴朗居（素社）", 0.0, 0.0),
            Station("基立下道", 0.0, 0.0),
            Station("江南大道口", 0.0, 0.0),
            Station("广东药学院", 0.0, 0.0),
            Station("海珠区妇幼", 0.0, 0.0),
            Station("江南新村", 0.0, 0.0),
            Station("宝业路", 0.0, 0.0),
            Station("工业大道口", 0.0, 0.0),
            Station("凤凰新村（宝业路口）", 0.0, 0.0),
            Station("沙园站", 0.0, 0.0),
            Station("广州市培英中学", 0.0, 0.0),
            Station("白鹤洞", 0.0, 0.0),
            Station("鹤洞西路", 0.0, 0.0),
            Station("芳村西塱总站", 0.0, 0.0),
        )
    )

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

    /**
     * 将线路掰成两半
     * 第一半的站数可能会比第二半多一个
     */
    fun splitLineToTwoPart(line: Line): Array<Line>{
        val arr = arrayOf(line.copy(), line.copy())
        val cnt = line.stations.size
        arr[0].stations = listCopyInRange(line.stations, 0, (cnt+1)/2)
        arr[1].stations = listCopyInRange(line.stations, (cnt+1)/2, cnt)
        return arr
    }

    private fun splitBaiduLineName(origin: String): String = splitTextBefore(origin, '(')

    private fun splitBaiduLineDirection(origin: String): String = splitTextBefore(origin, '方')

    private fun splitTextBefore(origin: String, target: Char): String {
        var end = 0
        for ((index, c) in origin.withIndex()) {
            if (c == target) {
                end = index
                break
            }
        }
        return origin.substring(0, end)
    }

    private fun <E> listCopyInRange(src: List<E>, from: Int, to: Int): List<E>{
        TODO("待补充")
    }
}