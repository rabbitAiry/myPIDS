package com.airy.buspids.ui

import android.graphics.Paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import com.airy.pids_lib.data.Station
import com.airy.pids_lib.ui.*
import kotlin.math.min

val textPaint = Paint().apply {
    color = dark_gray_100.toArgb()
}
val textEnPaint = Paint().apply {
    color = dark_gray_100.toArgb()
}
val metroTextPaint = Paint().apply {
    color = dark_gray_100.toArgb()
    textSize = 78F
}
val metroBackgroundPaint = Paint()
val linePaint = Paint().apply {
    color = gray.toArgb()
}
val stationIndicatorPaint = Paint()

const val factor = 0.8f

var singleTextSize = 0F

fun drawHorizontalLineMap(
    canvas: NativeCanvas,
    subStationList: List<Station>,
    subCurrIdx: Int,
    modeCn: Boolean,
    size: Size,
    shine: Boolean,
    subForecastTimeList: List<Int>?,
    otherLineColors: Map<String, Color>,
) {
    if (singleTextSize == 0F) singleTextSize = setProperTextSize(size, subStationList)
    val metroPadding = singleTextSize / 3
    linePaint.strokeWidth = singleTextSize * 0.6F
    var xPos: Float = singleTextSize * 2F  // 腾出 1.732*singleTextSize 的地方画指示线
    var yPos: Float = 0F
    val xOffset = singleTextSize * factor
    val yOffset = singleTextSize * factor * 1.732F
    val iRadius = singleTextSize / 2


    canvas.drawLine(0f, singleTextSize * 0.866f, size.width, singleTextSize * 0.866f, linePaint)

    for ((idx, station) in subStationList.withIndex()) {
        canvas.apply {
            save()
            rotate(60F)
            drawCircle(xPos + iRadius - singleTextSize*1.5F, yPos - iRadius, iRadius, stationIndicatorPaint.apply {
                color = when{
                    idx > subCurrIdx -> golden_400
                    idx < subCurrIdx -> gray
                    else -> if (shine) golden_600 else golden_200
                }.toArgb()
            })
            val text = if (subForecastTimeList != null && idx >= subCurrIdx){
                if (modeCn) "${subForecastTimeList[idx]/60}分钟 ${station.name}" else "${subForecastTimeList[idx]/60}min ${station.englishName}"
            }else {
                if (modeCn) station.name else station.englishName
            }
            drawText(text, 0, text.length, xPos, yPos, if (modeCn) textPaint else textEnPaint)
            var xEx = xPos + if (modeCn) textPaint.measureText(text) else textEnPaint.measureText(text)
            station.interchanges?.let { interchanges ->
                for (interchange in interchanges) {
                    xEx += metroPadding
                    xEx += drawInterchange(
                        x = xEx,
                        y = yPos,
                        interchange = interchange,
                        lineColor = otherLineColors[interchange] ?: gray,
                        singleTextSize = singleTextSize
                    )
                }
            }
            restore()
        }
        xPos += xOffset
        yPos -= yOffset
    }
}


/**
 * 绘制可换乘的线路
 */
fun NativeCanvas.drawInterchange(
    x: Float,
    y: Float,
    interchange: String,
    lineColor: Color,
    singleTextSize: Float
): Float {
    val text = interchange.trim().replace("号线", "").replace("线", "")
    val width = metroTextPaint.measureText(text)
    val textColor = if (lineColor.luminance() > 0.5) Color.Black else Color.White
    val bgHeight = singleTextSize + 4F
    val bgWidth = (width + 4F).coerceAtLeast(bgHeight)
    val diff = bgWidth - width
    drawRoundRect(
        x,
        y - singleTextSize,
        x + bgWidth,
        y + 4F,
        singleTextSize,
        singleTextSize,
        metroBackgroundPaint.apply { color = lineColor.toArgb() })
    drawText(
        text,
        0,
        text.length,
        x + diff / 2,
        y - 2F,
        metroTextPaint.apply { color = textColor.toArgb() })
    return width + diff
}

/**
 * 返回最佳字体大小
 */
private fun setProperTextSize(size: Size, stations: List<Station>): Float {
    val lastStationLen = stations.last().name.length+3
    val maxLen = stations.maxBy { it.name.length }.name.length+3
    val widthFriendlySize = size.width / (2 * factor * (stations.size + 2) + lastStationLen / 2)
    val heightFriendlySize = size.height / (2 + 0.866 * maxLen).toFloat()
    val properSize = min(widthFriendlySize, heightFriendlySize)
    val textSize = findTextSizeForSize(properSize)
    textPaint.textSize = textSize
    metroTextPaint.textSize = textSize - 2

    val lastStationEnLen = stations.last().names[1].length+5
    val maxEnLen = stations.maxBy { it.names[1].length }.names[1].length+5
    val widthFriendlyEnSize = size.width / (2 * factor * stations.size + lastStationEnLen / 2)
    val heightFriendlyEnSize = size.height-2*properSize / (0.866 * maxEnLen).toFloat()
    val properEnSize = min(widthFriendlyEnSize, heightFriendlyEnSize)
    textEnPaint.textSize = findTextSizeForSize(properEnSize)
    return properSize
}

private fun findTextSizeForSize(size: Float, isEn: Boolean = false): Float {
    var left = 1f
    var right = 500f
    val paint = Paint()
    while (left < right) {
        val mid = (left + right) / 2
        paint.textSize = mid
        val result = textSizeOnPaint(paint, isEn)
        if (result > size) right = mid
        else left = mid + 1
    }
    return left
}

/**
 * 测量单个文字的宽度
 */
private fun textSizeOnPaint(paint: Paint, isEn: Boolean): Float {
    return paint.measureText(if (isEn)"e" else "我")
}