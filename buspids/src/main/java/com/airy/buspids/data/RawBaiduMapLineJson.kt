package com.airy.buspids.data

data class RawBaiduMapLineJson(
    val bsl: RawBaiduMapLineInfo?,
    val result: RawResult?,
    val message: String?,
)

data class RawBaiduMapLineInfo(
    val content: List<RawBaiduMapLine>?
)

data class RawBaiduMapLine(
    val name: String,
    val line_direction:String,
    val line_color: String?,
    val geo: String,
    val company: String,
    val startTime: String,
    val endTime: String,
    val stations: List<RawBaiduMapStation>
)

data class RawBaiduMapStation(
    val name: String,
    val geo: String,
    val subways: List<RawMetroLine>? = null,
//    val transfer: List<>? = null  能获取换乘线路的uid
)

data class RawMetroLine(
    val background_color: String,
    val name: String
)

data class RawResult(
    val errMsg: String
)