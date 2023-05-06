package com.airy.buspids.data

data class RawBusLineData(
    val busId: String,
    val lineId: String,
    val startIdx: Int,
    val endIdx: Int
)