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