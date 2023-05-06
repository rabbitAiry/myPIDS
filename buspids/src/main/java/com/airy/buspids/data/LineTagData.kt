package com.airy.buspids.data

/**
 * 表示能够进行详情搜索、能够确定一条线路行驶区间的数据结构
 * 服务器会以此数据结构指定线路
 */
data class LineTagData(
    val lineId: String,
    val startIdx: Int,
    val endIdx: Int,
    val city: String = "广州",
    val source: String = "BaiduMap",
)
