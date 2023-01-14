package com.airy.mypids.pids.gz_metro_l2_style

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.airy.mypids.objects.LineInfo

class LineMapView: View {
    constructor(context: Context): super(context)
    @JvmOverloads constructor(context: Context,
                              attributeSet: AttributeSet?,
                              defStyleAttr: Int = 0,
                              defStyleRes: Int = 0):super(context, attributeSet, defStyleAttr, defStyleRes)
    private var lineInfo: LineInfo? = null
    private var linePaint = Paint()
    private val linePath = Path()
    private val space = 10

    fun setLine(lineInfo: LineInfo, lineColor: Int){
        this.lineInfo = lineInfo
        linePaint.color = lineColor
        linePaint.strokeWidth = 10f
        linePaint.isAntiAlias = true
        onDrawPrepare()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        lineInfo?.let{
            val perRowStationCnt = (it.stations.size+1)/2
            val perLineWidth = (width.toFloat()-height/4-space)/(perRowStationCnt+1)
            val perHeight = height.toFloat()/4

            // 1. draw line
            linePath.moveTo(perLineWidth, perHeight)
            linePath.lineTo(width.toFloat()-height/4-paddingRight, perHeight)
            linePath.quadTo(width.toFloat()-space, perHeight*2, width.toFloat()-height/4-paddingRight, perHeight*3)
            linePath.lineTo(perLineWidth, perHeight*3)
            canvas?.drawPath(linePath, linePaint)

            // 2. draw "->"
            for(i in 1 .. perRowStationCnt){

            }

            // 3. draw stations
        }
    }

    private fun onDrawPrepare(){

    }

}