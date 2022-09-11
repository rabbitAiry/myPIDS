package com.airy.mypids.pids.gz_metro_l2_style

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.airy.mypids.objects.Line

class LineMapView: View {
    constructor(context: Context): super(context)
    @JvmOverloads constructor(context: Context,
                              attributeSet: AttributeSet?,
                              defStyleAttr: Int = 0,
                              defStyleRes: Int = 0):super(context, attributeSet, defStyleAttr, defStyleRes)
    private var line: Line? = null

    fun setLine(line: Line){
        this.line = line
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        line?.let{
//            canvas.draw
        }
    }

}