package com.airy.mypids.pids.gz_bus_style

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import com.airy.mypids.objects.Line

const val MIN_TEXT_SIZE_THRESHOLD = 50
class LineMapLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    var textSize = 0f
    var line: Line? = null
        set(value) {
            field = value
//            initTextViewArray(value!!)
        }

    private fun initTextViewArray(line: Line) {
        val n = line.stationCount
        val perWidth = width.toFloat() / n
        textSize = getTextSize(perWidth * 3 / 4)
        for (i in 0 until n) {
            val view = FlowingText(
                context,
                isFirst = i == 0,
                isEnd = i == n - 1,
                text = line.getStation(i).name,
                status = if (i > line.currStationIdx) FlowingText.Status.NOT_ARRIVE
                else if (i == line.currStationIdx) FlowingText.Status.CURR
                else FlowingText.Status.ARRIVED
            )
            val params = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
            params.weight = 1f
            view.layoutParams = params
            addView(view)
        }
    }

    private fun needTwoRow(line: Line):Boolean{
        val perWidth = (width - marginLeft - marginRight).toFloat() / line.stationCount
        return getTextSize(perWidth * 3 / 4)< MIN_TEXT_SIZE_THRESHOLD
    }

    /**
     * 期望是每一行只有一个字
     */
    private fun getTextSize(width: Float): Float {
        val max = 200
        var left = 0
        var right = max
        val paint = Paint()
        while (left < right) {
            val mid = (left + right + 1) / 2
            paint.textSize = mid.toFloat()
            val tempWidth = paint.measureText("测")
            if (tempWidth > width) right = mid - 1
            else left = mid
        }
        return left.toFloat()
    }
}