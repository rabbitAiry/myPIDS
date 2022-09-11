package com.airy.mypids.pids.gz_bus_style

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.airy.mypids.R
import com.airy.mypids.objects.Line
import com.airy.mypids.utils.ColorUtil
import com.airy.mypids.views.RoundStatusDrawable

const val SHINING = 1
class LineMapLinearLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    private var shiningOn = false
    private var textSize = 0f
    var line : Line? = null
        set(value) {
            field = value
            initTextViewArray(value!!)
            invalidate()
        }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if(visibility == View.GONE) handler.removeMessages(SHINING) else shine()
    }

    private fun initTextViewArray(line: Line){
        val stations = line.stations
        val n = stations.size
        val perWidth = width.toFloat()/n
        val perHeight = height
        textSize = getTextSize(perWidth*3/4)
        for(i in 0 until n){
            val view = LayoutInflater.from(context).inflate(R.layout.item_gz_bus_style_text, null, false)
            val params = ViewGroup.LayoutParams(perWidth.toInt(), perHeight)
            view.layoutParams = params
            drawView(view, i)
            addView(view, perWidth.toInt(), perHeight)
        }
    }

    private fun drawView(idx: Int) = drawView(get(idx), idx)

    private fun drawView(view: View, idx: Int){
        val line = line!!
        val width = view.width.toFloat()
        val textStation:TextView = view.findViewById(R.id.text_station)
        val viewStationStatus: View = view.findViewById(R.id.view_station_status)
        val viewLineLeft: View = view.findViewById(R.id.view_line_left)
        val viewLineRight: View = view.findViewById(R.id.view_line_right)

        textStation.text = line.stations[idx].name
        if(idx==0)viewLineLeft.visibility = View.INVISIBLE
        if(idx==line.stations.size-1)viewLineRight.visibility = View.INVISIBLE
        val colorId = if(idx>line.currStationIdx) R.color.light_blue_800 else R.color.gray_500
        viewStationStatus.background = RoundStatusDrawable(ColorUtil.getResColor(colorId, context), width/8*3, width/2, width/2)
        textStation.textSize = textSize
        if(idx==line.currStationIdx){
            textStation.setBackgroundColor(ColorUtil.getResColor(R.color.light_blue_500, context))
            textStation.setTextColor(Color.WHITE)
        }else textStation.setTextColor(ColorUtil.getResColor(colorId, context))
    }

    /**
     * 找到单字比期望的参数width要小的字体大小
     */
    private fun getTextSize(width: Float): Float{
        val max = 60
        var left = 0
        var right = max
        val fakeTv = TextView(context)
        while(left<right){
            val mid = (left+right+1)/2
            fakeTv.textSize = mid.toFloat()
            val tempWidth = fakeTv.paint.measureText("一")
            if(tempWidth>width)right = mid-1
            else left = mid
        }
        return left.toFloat()
    }

    fun nextStation(){
        val idx = line!!.currStationIdx
        drawView(idx-1)
        drawView(idx)
    }

    private fun shine(){

    }

}