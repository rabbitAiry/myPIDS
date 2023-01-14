package com.airy.mypids.utils

import com.airy.mypids.objects.LineInfo
import com.airy.mypids.objects.Station
import com.baidu.mapapi.search.busline.BusLineResult

object LineUtil {
    val fakeLine1Info: LineInfo = LineInfo(
        "811路", "#888888", listOf(
            Station(arrayOf("兴民路总站"), 0.0, 0.0),
            Station(arrayOf("兴国路"), 0.0, 0.0),
            Station(arrayOf("猎德"), 0.0, 0.0),
            Station(arrayOf("花城大道"), 0.0, 0.0),
            Station(arrayOf("花城大道（华穗路口）"), 0.0, 0.0),
            Station(arrayOf("五羊邨"), 0.0, 0.0),
            Station(arrayOf("长城大厦"), 0.0, 0.0),
            Station(arrayOf("培正路"), 0.0, 0.0),
            Station(arrayOf("新河浦路"), 0.0, 0.0),
            Station(arrayOf("东湖路"), 0.0, 0.0),
            Station(arrayOf("仲恺路"), 0.0, 0.0),
            Station(arrayOf("怡安花园晴朗居（素社）"), 0.0, 0.0),
            Station(arrayOf("基立下道"), 0.0, 0.0),
            Station(arrayOf("江南大道口"), 0.0, 0.0),
            Station(arrayOf("广东药学院"), 0.0, 0.0),
            Station(arrayOf("海珠区妇幼"), 0.0, 0.0),
            Station(arrayOf("江南新村"), 0.0, 0.0),
            Station(arrayOf("宝业路"), 0.0, 0.0),
            Station(arrayOf("工业大道口"), 0.0, 0.0),
            Station(arrayOf("凤凰新村（宝业路口）"), 0.0, 0.0),
            Station(arrayOf("沙园站"), 0.0, 0.0),
            Station(arrayOf("广州市培英中学"), 0.0, 0.0),
            Station(arrayOf("白鹤洞"), 0.0, 0.0),
            Station(arrayOf("鹤洞西路"), 0.0, 0.0),
            Station(arrayOf("芳村西塱总站"), 0.0, 0.0),
        )
    )

    fun baiduLineToAppLine(result: BusLineResult): LineInfo {
        val stations = ArrayList<Station>()
        for (s in result.stations) {
            stations.add(Station(arrayOf(s.title), s.location.latitude, s.location.longitude))
        }
        return LineInfo(
            splitBaiduLineName(result.busLineName),
            splitBaiduLineDirection(result.lineDirection),
            stations
        )
    }

    /**
     * 将线路掰成两半
     * 第一半的站数可能会比第二半多一个
     */
    fun splitLineToTwoPart(lineInfo: LineInfo): Array<LineInfo>{
//        val arr = arrayOf(line.copy(), line.copy())
//        val cnt = line.stations.size
//        arr[0].stations = listCopyInRange(line.stations, 0, (cnt+1)/2)
//        arr[1].stations = listCopyInRange(line.stations, (cnt+1)/2, cnt)
//        return arr
        return Array(1){ LineInfo("","", listOf(Station(arrayOf(""),0.0, 0.0))) }
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