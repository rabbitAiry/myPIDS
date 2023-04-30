package com.airy.pids_lib.data

import androidx.compose.ui.graphics.Color
import com.airy.pids_lib.ui.gray

data class LineInfo(
    // 线路全称（含方向）
    val lineName: String,
    // 线路名称（不含方向）
    val rawLineName: String,
    // 方向
    val lineDirection: String,
    // 站点列表
    var stations: List<Station>,
    // 站点涵括的语种
    val languages: MutableList<String>,
    // 线路起始站点的位置，常用于区间车
    val startStationIdx:Int = 0,
    // 线路结束站点的位置，常用于区间车
    val endStationIdx: Int = stations.size-1,
    // 线路id（仅用于UI展示）
    var lineId: String,
    // 线路颜色（仅用于UI展示）
    var lineColor: Color,
    // 其他换乘线路的颜色（仅用于UI展示）
    val otherLineColors: Map<String, Color>,
    // 线路在UI上的走向是否是从左往右的（仅用于UI展示）
    var lineMapLeftToRight: Boolean = true,
    // 站点起始id（仅用于UI展示）
    val stationIdStart: Int = 0,
    // 站点id是否递增，反之递减（仅用于UI展示）
    val isStationIdIncrease: Boolean = true,
){
    fun getLineColor(line: String): Color{
        return otherLineColors[line] ?: gray
    }
}