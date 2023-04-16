package com.airy.buspids.ui

import android.graphics.Paint
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import com.airy.buspids.constants.UI_TAG
import com.airy.pids_lib.data.LineInfo
import com.airy.pids_lib.data.StationStatus
import com.airy.pids_lib.ui.*

val textPaint = Paint().apply {
    color = dark_gray_100.toArgb()
    textSize = 80F
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


fun drawHorizontalLineMap(
    canvas: NativeCanvas,
    line: LineInfo,
    stationStates: List<StationStatus>,
    modeCn: Boolean,
    size: Size,
    density: Float,
    shine: Boolean
) {
    val singleTextSize = textPaint.measureText("口")
    // TODO 单字大小映射padding: singleTextSize = 30
    val layoutPadding = 16F
    val metroPadding = 10F
    val drawLineHeight = 10F
    val stationIndicatorHeight = 6F
    var xPos = layoutPadding
    var yPos = layoutPadding
    val factor = 0.8F
    val xOffset = singleTextSize * factor
    val yOffset = singleTextSize * factor * 1.732F

    canvas.drawLine(0f, drawLineHeight, size.width, drawLineHeight, linePaint)

    for ((idx, station) in line.stations.withIndex()) {
        val text = if (modeCn) station.name else station.englishName
        canvas.apply {
            save()
            rotate(60F)
            drawCircle(xPos, yPos, 4F, stationIndicatorPaint.apply {
                color = when(stationStates[idx]){
                    StationStatus.UNREACHED -> golden_400
                    StationStatus.ARRIVED -> gray
                    StationStatus.CURR -> if (shine) golden_600 else golden_200
                    else ->gray
                }.toArgb()
            })
            drawText(text, 0, text.length, xPos, yPos, textPaint)
            var xEx = xPos + textPaint.measureText(text)
            station.interchanges?.let { interchanges ->
                for (interchange in interchanges) {
                    xEx += metroPadding
                    xEx += drawInterchange(
                        xEx,
                        yPos,
                        interchange,
                        line.getLineColor(interchange),
                        singleTextSize
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
    // TODO: 不要在这里处理文字
    val text = interchange.trim().replace("号线", "").replace("线", "")
    val width = metroTextPaint.measureText(text)
    val textColor = if (lineColor.luminance() > 0.5) androidx.compose.ui.graphics.Color.Black else androidx.compose.ui.graphics.Color.White
    val bgHeight = singleTextSize + 4F
    val bgWidth = (width + 4F).coerceAtLeast(bgHeight)
    val diff = bgWidth - width
    drawRoundRect(
        x,
        y - singleTextSize,
        x + bgWidth,
        y + 4F,
        20F,
        20F,
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